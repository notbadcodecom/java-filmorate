package ru.yandex.practicum.filmorate.storage;

import java.util.ArrayList;
import java.util.Optional;

public interface Storage<T> extends LikeStorage {

    Optional<T> get(int id);

    T add(T t);

    ArrayList<T> getAll();

}
