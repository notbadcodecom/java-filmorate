package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public interface Storage<T> extends Like {

    Optional<T> get(int id);

    T add(T t);

    ArrayList<T> getAll();

}
