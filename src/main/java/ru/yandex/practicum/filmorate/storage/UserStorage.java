package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    Optional<User> get(int id);

    User add(User user);

    ArrayList<User> getAll();

    void saveLikes(int id, Set<Integer> likes);

    Optional<Set<Integer>> loadLikes(int id);

}
