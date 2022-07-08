package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    Optional<Film> get(int id);

    Film add(Film film);

    ArrayList<Film> getAll();

    void saveScores(int id, Set<Integer> scores);

    Optional<Set<Integer>> loadScores(int id);

}
