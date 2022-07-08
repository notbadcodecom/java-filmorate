package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User newUser = storage.add(user);
        log.debug("Create user {}", newUser);
        return newUser;
    }

    public User update(User user) {
        log.debug("User before update {}", user);
        User updatedUser = get(user.getId());
        if (user.getBirthday() == null) user.setBirthday(updatedUser.getBirthday());
        if (user.getLogin() == null) user.setLogin(updatedUser.getLogin());
        if (user.getEmail() == null) user.setEmail(updatedUser.getEmail());
        log.debug("User after update {}", user);
        return storage.add(user);
    }

    public ArrayList<User> getAll() {
        ArrayList<User> users = storage.getAll();
        log.debug("Return {} users", users.size());
        return users;
    }

    public User get(int id) {
        Optional<User> user = storage.get(id);
        if (user.isPresent()) {
            log.debug("Load from storage user {}", user);
            return user.get();
        } else {
            log.debug("User #{} not found", id);
            throw new NotFoundException("User not found");
        }
    }

    public void addFriendsToEachOther(int id, int friendId) {
        if (hasNotUserId(id) || hasNotUserId(friendId)) throw new NotFoundException("User not found.");
        addFriend(id, friendId);
        addFriend(friendId, id);
    }

    private void addFriend(int id, int friendId) {
        Set<Integer> likes = getLikes(id);
        likes.add(friendId);
        log.debug("Create friendship from user #{} to user #{}", friendId, id);
        storage.saveFriends(id, likes);
    }

    public void deleteFriendsFromEachOther(int id, int friendId) {
        if (hasNotUserId(id) || hasNotUserId(friendId)) throw new NotFoundException("User not found");
        deleteFriend(id, friendId);
        deleteFriend(friendId, id);
    }

    private void deleteFriend(int id, int friendId) {
        Set<Integer> likes = getLikes(id);
        likes.remove(friendId);
        storage.saveFriends(id, likes);
        log.debug("Delete friendship from user #{} to user #{}",  id, friendId);
    }

    public List<User> getFriends(int id) {
        List<User> friends = getLikes(id).stream()
                .map(storage::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        log.debug("Return {} friends", friends.size());
        return friends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> friends = getLikes(id).stream()
                .filter(getLikes(otherId)::contains)
                .map(storage::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        log.debug("Return {} common friends", friends.size());
        return friends;
    }

    private Set<Integer> getLikes(int id) {
        return storage.loadFriends(id).orElseGet(HashSet::new);
    }

    private boolean hasNotUserId(int id) {
        return storage.get(id).isEmpty();
    }
}
