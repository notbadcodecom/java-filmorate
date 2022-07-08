package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public interface Storage<T> {

    Optional<T> get(int id);

    T add(T t);

    ArrayList<T> getAll();

    void saveMarks(int id, Set<Integer> marks);

    Optional<Set<Integer>> loadMarks(int id);

}
