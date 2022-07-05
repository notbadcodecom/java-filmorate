package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component("userStorage")
public class InMemoryUserStorage implements Storage<User> {

    private int userIdCounter = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Optional<User> get(int id) {
        log.debug("Getting user [{}]", users.get(id));
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++userIdCounter);
        users.put(user.getId(), user);
        log.debug("Add user [{}]", user);
        return user;
    }

    @Override
    public User update(User user) {
        User updatedUser = users.get(user.getId());
        if (user.getBirthday() == null) user.setBirthday(updatedUser.getBirthday());
        if (user.getLogin() == null) user.setLogin(updatedUser.getLogin());
        if (user.getEmail() == null) user.setEmail(updatedUser.getEmail());
        users.put(user.getId(), user);
        log.debug("Update user [{}]", user);
        return user;
    }

    @Override
    public ArrayList<User> getAll() {
        log.debug("Return {} users.", users.size());
        return new ArrayList<>(users.values());
    }
}
