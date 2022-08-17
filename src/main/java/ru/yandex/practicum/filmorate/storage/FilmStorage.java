package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> loadFilm(long id);

    long saveFilm(Film film);

    void updateFilm(Film film);

    void deleteFilm(long id);

    List<Film> loadFilms();

    void saveRatingPoint(long filmId, long userId);

    void deleteRatingPoint(long filmId, long userId);

    boolean hasFilmRatingFromUser(long filmId, long userId);

    List<Film> loadPopularFilms(long count);

    List<Film> loadFilmsOfDirectorSortedByYears(long directorId);

    List<Film> loadFilmsOfDirectorSortedByRating(long directorId);
}
