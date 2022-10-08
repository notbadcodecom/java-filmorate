package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private final EventService eventService;
    @Autowired
    public UserService(UserStorage userStorage, EventService eventService) {
        this.userStorage = userStorage;
        this.eventService = eventService;
    }

    public User getUserOrNotFoundException(long id) {
        Optional<User> user = userStorage.loadUser(id);
        if (user.isPresent()) {
            log.debug("Load {}", user.get());
            return user.get();
        } else {
            throw new NotFoundException("User #" + id + " not found");
        }
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User savedUser = getUserOrNotFoundException(userStorage.saveUser(user));
        log.debug("Create {}", savedUser);
        return savedUser;
    }

    public User update(User user) {
        User updatingUser = getUserOrNotFoundException(user.getId());
        if (user.getBirthday() == null) user.setBirthday(updatingUser.getBirthday());
        if (user.getLogin() == null) user.setLogin(updatingUser.getLogin());
        if (user.getEmail() == null) user.setEmail(updatingUser.getEmail());
        userStorage.updateUser(user);
        User savedUser = getUserOrNotFoundException(user.getId());
        log.debug("Update {}", savedUser);
        return getUserOrNotFoundException(user.getId());
    }

    public List<User> getAllUsers() {
        List<User> users = userStorage.loadUsers();
        log.debug("Return all ({}) users", users.size());
        return users;
    }

    public void addFriendship(long userId, long friendId) {
        getUserOrNotFoundException(userId);
        getUserOrNotFoundException(friendId);
        if (userStorage.isExistFriendship(userId, friendId) || userStorage.isExistFriendship(friendId, userId)) {
            log.debug("Attempt to create an existing request for user #{} from user #{}", userId, friendId);
        } else {
            userStorage.saveFriendshipRequest(userId, friendId, FriendshipStatus.REQUEST);
            log.debug("Creating friendship request for user #{} from user #{}",  userId, friendId);
            eventService.saveEvent(userId, friendId, EventType.FRIEND, EventOperation.ADD);
            log.debug("Saving event: creating friendship request for user #{} from user #{}",  userId, friendId);
        }
    }

    public void confirmFriendship(long userId, long friendId) {
        getUserOrNotFoundException(userId);
        getUserOrNotFoundException(friendId);
        if (userStorage.isExistFriendship(userId, friendId)) {
            eventService.saveEvent(userId, friendId, EventType.FRIEND, EventOperation.REMOVE);
            userStorage.updateFriendshipStatus(userId, friendId, FriendshipStatus.ACCEPTED);
            userStorage.deleteFriendshipRequest(friendId, userId);
            log.debug("User #{} confirmed friendship request of user #{}", userId, friendId);
        } else if (userStorage.isExistFriendship(friendId, userId)) {
            userStorage.updateFriendshipStatus(friendId, userId, FriendshipStatus.ACCEPTED);
            eventService.saveEvent(userId, friendId, EventType.FRIEND, EventOperation.ADD);
            log.debug("User #{} confirmed friendship request of user #{}", userId, friendId);
        } else {
            log.debug("Attempt to confirm a non-existent request from user #{} to user #{}", friendId, userId);
        }
    }

    public void refuseFriendship(long userId, long friendId) {
        getUserOrNotFoundException(userId);
        getUserOrNotFoundException(friendId);
        if (userStorage.isExistFriendship(userId, friendId)) {
            userStorage.deleteFriendshipRequest(userId, friendId);
            log.debug("User #{} refused friendship request from user #{}", userId, friendId);
            eventService.saveEvent(userId, friendId, EventType.FRIEND, EventOperation.REMOVE);
            log.debug("Saving event: user #{} refused friendship request from user #{}", userId, friendId);
        } else {
            log.debug("Attempt to refuse a non-existent request from user #{} to user #{}", friendId, userId);
        }
    }

    public List<User> getFriends(long id) {
        getUserOrNotFoundException(id);
        List<User> friends = userStorage.loadUserFriends(id);
        log.debug("Return {} friends", friends.size());
        return friends;
    }

    public List<User> getCommonFriends(long id, long otherId) {
        List<User> friends = getFriends(id);
        friends.retainAll(getFriends(otherId));
        log.debug("Return {} common friends", friends.size());
        return friends;
    }

    public boolean isNotExistEmail(String email) {
        return userStorage.isNotExistEmail(email);
    }

    public boolean isNotExistLogin(String login) {
        return userStorage.isNotExistLogin(login);
    }

    public void deleteUser(long id) {
        getUserOrNotFoundException(id);
        userStorage.deleteUser(id);
        log.debug("Delete user #{} ",  id);
    }

    public List<Event> getEvents(long id) {
        return eventService.getEvents(id);
    }
}
