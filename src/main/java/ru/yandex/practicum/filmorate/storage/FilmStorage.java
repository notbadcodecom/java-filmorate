package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

    List<Film> loadPopularFilms(long count, long genreId);

    List<Film> loadPopularFilms(long count, String year);

    List<Film> loadPopularFilms(long count, long genreId, String year);

    List<Film> loadFilmsOfDirectorSortedByYears(long directorId);

    List<Film> loadFilmsOfDirectorSortedByRating(long directorId);

    List<Film> searchFilmByProperty(String query, String filmSearchProperties);

    Map<Integer, Set<Integer>> getUserLikes();

}
