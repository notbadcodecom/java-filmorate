package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private int userIdCounter;
    private final Map<Integer, User> users;
    private final Map<Integer, Set<Integer>> likes;

    public InMemoryUserStorage() {
        userIdCounter = 0;
        users = new HashMap<>();
        likes = new HashMap<>();
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

    @Override
    public void saveLikes(int id, Set<Integer> newLikes) {
        likes.put(id, newLikes);
        log.debug("Save for id #{} to memory {} like(s)", id, newLikes.size());
    }

    @Override
    public Optional<Set<Integer>> loadLikes(int id) {
        int count = (likes.get(id) == null) ? 0 : likes.get(id).size();
        log.debug(
                "Load from memory {} like(s) for id #{}",
                count,
                id
        );
        return Optional.ofNullable(likes.get(id));
    }
}
