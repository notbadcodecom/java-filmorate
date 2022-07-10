package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Optional;
import java.util.Set;

public interface FilmStorage extends Storage<Film> {
    void saveScores(int id, Set<Integer> scores);

    Optional<Set<Integer>> loadScores(int id);
}
