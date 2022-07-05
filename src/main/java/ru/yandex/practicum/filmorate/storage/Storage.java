package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.Optional;

public interface Storage<T> {

    Optional<T> get(int id);

    T add(T t);

    T update(T t);

    ArrayList<T> getAll();

}
