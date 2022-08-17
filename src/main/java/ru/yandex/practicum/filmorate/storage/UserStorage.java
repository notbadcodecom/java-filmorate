package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Optional<User> loadUser(long id);

    long saveUser(User user);

    void updateUser(User user);

    void deleteUser(long id);

    List<User> loadUsers();

    void saveFriendshipRequest(long userId, long friendId, FriendshipStatus status);

    boolean isExistFriendship(long userId, long friendId);

    void deleteFriendshipRequest(long userId, long friendId);

    void updateFriendshipStatus(long userId, long friendId, FriendshipStatus status);

    List<User> loadUserFriends(long userId);

    boolean isNotExistEmail(String email);

    boolean isNotExistLogin(String login);
}
