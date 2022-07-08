package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Qualifier("userStorage")
    private final Storage<User> storage;

    @Autowired
    public UserService(Storage<User> storage) {
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
        log.debug("List of users {}", users);
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

    public void addLikes(int id, int friendId) {
        if (hasNotUserId(id) || hasNotUserId(friendId)) throw new NotFoundException("User not found.");
        addLike(id, friendId);
        addLike(friendId, id);
    }

    private void addLike(int id, int friendId) {
        Set<Integer> likes = getLikesIds(id);
        likes.add(friendId);
        log.debug("Create like from user #{} to user #{}", friendId, id);
        storage.saveLikes(id, likes);
    }

    public void deleteLikes(int id, int friendId) {
        if (hasNotUserId(id) || hasNotUserId(friendId)) throw new NotFoundException("User not found");
        deleteLike(id, friendId);
        deleteLike(friendId, id);
    }

    private void deleteLike(int id, int friendId) {
        Set<Integer> likes = getLikesIds(id);
        likes.remove(friendId);
        storage.saveLikes(id, likes);
        log.debug("Delete like from user #{} to user #{}",  id, friendId);
    }

    public List<User> getFriends(int id) {
        List<User> friends = getLikesIds(id).stream()
                .map(storage::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        log.debug("Return {} friends", friends.size());
        log.debug("List of friends: {}", friends);
        return friends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriends = getLikesIds(id).stream()
                .filter(getLikesIds(otherId)::contains)
                .map(storage::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        log.debug("Return {} common friends", commonFriends.size());
        log.debug("List of common friends: {}", commonFriends);
        return commonFriends;
    }

    private Set<Integer> getLikesIds(int id) {
        return storage.loadLikes(id).orElseGet(HashSet::new);
    }

    private boolean hasNotUserId(int id) {
        return storage.get(id).isEmpty();
    }
}
