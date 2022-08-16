package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreService genreService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService, GenreService genreService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreService = genreService;
    }

    public Film getFilmOrNotFoundException(long id) {
        Optional<Film> film = filmStorage.loadFilm(id);
        if (film.isPresent()) {
            log.debug("Load {}", film.get());
            return film.get();
        } else {
            throw new NotFoundException("User #" + id + " not found");
        }
    }

    public Film create(Film film) {
        long filmId = filmStorage.saveFilm(film);
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            genreService.addFilmGenres(filmId, film.getGenres());
        }
        Film savedFilm = getFilmOrNotFoundException(filmId);
        log.debug("Create {}", savedFilm);
        return savedFilm;
    }

    public Film update(Film film) {
        Film updatingFilm = getFilmOrNotFoundException(film.getId());
        if (film.getDescription() == null) film.setDescription(updatingFilm.getDescription());
        if (film.getReleaseDate() == null) film.setReleaseDate(updatingFilm.getReleaseDate());
        if (film.getDuration() == 0L) film.setDuration(updatingFilm.getDuration());
        if (film.getName() == null || film.getName().isBlank()) film.setName(updatingFilm.getName());
        if (film.getMpa() == null) film.setMpa(updatingFilm.getMpa());
        if (film.getGenres() == null) {
            film.setGenres(updatingFilm.getGenres());
        } else if (film.getGenres().size() == 0) {
            genreService.deleteFilmGenres(film.getId());
        } else {
            genreService.updateFilmGenres(film.getId(), film.getGenres());
        }
        filmStorage.updateFilm(film);
        Film savedFilm = getFilmOrNotFoundException(film.getId());
        log.debug("Update {}", savedFilm);
        return savedFilm;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.loadFilms();
        log.debug("Load {} movies", films.size());
        return films;
    }

    public void addRatingPoint(long filmId, long userId) {
        getFilmOrNotFoundException(filmId);
        userService.getUserOrNotFoundException(userId);
        if (filmStorage.hasFilmRatingFromUser(filmId, userId)) {
            log.debug("Attempt to create an existing rating point for movie #{} from user #{}",  filmId, userId);
        } else {
            filmStorage.saveRatingPoint(filmId, userId);
            log.debug("Creating rating point for movie #{} from user #{}",  filmId, userId);
        }
    }

    public void deleteRatingPoint(long filmId, long userId) {
        getFilmOrNotFoundException(filmId);
        userService.getUserOrNotFoundException(userId);
        if (filmStorage.hasFilmRatingFromUser(filmId, userId)) {
            filmStorage.deleteRatingPoint(filmId, userId);
            log.debug("Delete rating point of movie #{} from user #{}",  filmId, userId);
        } else {
            log.debug("Attempt to delete a non-existent rating point for movie #{} from user #{}", filmId, userId);
        }
    }

    public List<Film> getPopular(long count) {
        List<Film> popular = filmStorage.loadPopularFilms(count);
        log.debug("Return {} popular films", popular.size());
        return popular;
    }

    public void deleteUser(long id){
        getFilmOrNotFoundException(id);
        filmStorage.deleteFilm(id);
        log.debug("Delete  movie #{}", id);
    }
}
