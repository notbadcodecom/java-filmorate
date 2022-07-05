package ru.yandex.practicum.filmorate.storage;

import java.util.Set;

public interface Like {

    void saveLikes(int id, Set<Integer> idLikes);

    Set<Integer> loadLikes(int id);

}
