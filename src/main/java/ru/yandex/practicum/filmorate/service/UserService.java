package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.Optional;

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
}
