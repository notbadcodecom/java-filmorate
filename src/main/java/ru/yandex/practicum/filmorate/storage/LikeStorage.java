package ru.yandex.practicum.filmorate.storage;

import java.util.Optional;
import java.util.Set;

public interface LikeStorage {

    void saveLikes(int id, Set<Integer> idLikes);

    Optional<Set<Integer>> loadLikes(int id);

}
