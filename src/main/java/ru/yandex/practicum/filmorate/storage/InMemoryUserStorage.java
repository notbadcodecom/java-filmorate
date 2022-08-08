package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private long userIdCounter;
    private final Map<Long, User> users;
    private final Map<Long, Set<Long>> friends;

    public InMemoryUserStorage() {
        userIdCounter = 0L;
        users = new HashMap<>();
        friends = new HashMap<>();
    }

    @Override
    public Optional<User> loadUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public long saveUser(User user) {
        if (user.getId() == 0) user.setId(++userIdCounter);
        users.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public List<User> loadUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void saveFriendshipRequest(long userId, long friendId, FriendshipStatus status) {
        Set<Long> likes = friends.get(userId);
        likes.add(friendId);
        friends.put(userId, likes);
    }

    @Override
    public boolean isExistFriendship(long userId, long friendId) {
        Set<Long> scores = friends.get(userId);
        return scores.contains(friendId);
    }

    @Override
    public void deleteFriendshipRequest(long userId, long friendId) {
        Set<Long> scores = friends.get(userId);
        scores.remove(userId);
    }

    @Override
    public void updateFriendshipStatus(long userId, long friendId, FriendshipStatus status) {}

    @Override
    public List<User> loadUserFriends(long userId) {
        return friends.get(userId).stream()
                .map(this::loadUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isNotExistEmail(String email) {
        return users.values().stream()
                .map(User::getEmail)
                .noneMatch(e -> e.equals(email));
    }

    @Override
    public boolean isNotExistLogin(String login) {
        return users.values().stream()
                .map(User::getLogin)
                .noneMatch(e -> e.equals(login));
    }
}
