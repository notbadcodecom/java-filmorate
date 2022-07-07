package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component("userStorage")
public class InMemoryUserStorage extends InMemoryLikeStorageStorage implements Storage<User> {

    private int userIdCounter;
    private final Map<Integer, User> users;

    public InMemoryUserStorage() {
        userIdCounter = 0;
        users = new HashMap<>();
    }

    @Override
    public Optional<User> get(int id) {
        log.debug("Getting from memory user {}", users.get(id));
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User add(User user) {
        if (user.getId() == 0) user.setId(++userIdCounter);
        log.debug("Generate ID for user {}", user);
        users.put(user.getId(), user);
        log.debug("Save to memory user [{}]", user);
        return user;
    }

    @Override
    public ArrayList<User> getAll() {
        log.debug("Getting all ({}) users", users.size());
        return new ArrayList<>(users.values());
    }
}
