package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

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
        log.debug("Create user [{}]", user);
        return storage.add(user);
    }

    public User update(User user) {
        User updatedUser = getUserIfPresent(user.getId());
        if (user.getBirthday() == null) user.setBirthday(updatedUser.getBirthday());
        if (user.getLogin() == null) user.setLogin(updatedUser.getLogin());
        if (user.getEmail() == null) user.setEmail(updatedUser.getEmail());
        log.debug("Update user [{}]", user);
        return storage.add(user);
    }

    public ArrayList<User> getAll() {
        log.debug("Return {} users.", storage.getAll().size());
        return storage.getAll();
    }

    public User get(int id) {
        log.debug("Return user [{}]", id);
        return getUserIfPresent(id);
    }

    private User getUserIfPresent(int id) {
        Optional<User> user = storage.get(id);
        if (user.isPresent()) {
            log.debug("Return user [{}]", user);
            return user.get();
        } else {
            log.debug("User {} not found.", id);
            throw new NotFoundException("User " + id);
        }
    }
}
