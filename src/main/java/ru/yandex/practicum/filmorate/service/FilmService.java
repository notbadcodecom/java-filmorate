package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreService genreService;

    private final DirectorService directorService;

    @Autowired
    public FilmService(
            FilmStorage filmStorage,
            UserService userService,
            GenreService genreService,
            DirectorService directorService
    ) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreService = genreService;
        this.directorService = directorService;
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
        if (film.getDirectors() != null && film.getDirectors().size() > 0) {
            directorService.addFilmDirectors(filmId, film.getDirectors());
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
        if (film.getDirectors() == null || film.getDirectors().size() == 0) {
            directorService.deleteFilmDirectors(film.getId());
        } else {
            directorService.updateFilmDirectors(film.getId(), film.getDirectors());
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

    public List<Film> getUsersCommonFilms(long userId, long friendId) {
        List<Film> usersCommonFilms = filmStorage.getUsersCommonFilms(userId, friendId);
        log.debug("Return common films list for users {} and {}", userId, friendId);
        return usersCommonFilms;
    }

    public void addRatingPoint(long filmId, long userId) {
        getFilmOrNotFoundException(filmId);
        userService.getUserOrNotFoundException(userId);
        if (filmStorage.hasFilmRatingFromUser(filmId, userId)) {
            log.debug("Attempt to create an existing rating point for movie #{} from user #{}", filmId, userId);
        } else {
            filmStorage.saveRatingPoint(filmId, userId);
            log.debug("Creating rating point for movie #{} from user #{}", filmId, userId);
        }
    }

    public void deleteRatingPoint(long filmId, long userId) {
        getFilmOrNotFoundException(filmId);
        userService.getUserOrNotFoundException(userId);
        if (filmStorage.hasFilmRatingFromUser(filmId, userId)) {
            filmStorage.deleteRatingPoint(filmId, userId);
            log.debug("Delete rating point of movie #{} from user #{}", filmId, userId);
        } else {
            log.debug("Attempt to delete a non-existent rating point for movie #{} from user #{}", filmId, userId);
        }
    }

    public List<Film> getPopular(long count, Optional<Long> genreId, Optional<String> year) {
        List<Film> popular;
        if (genreId.isPresent() && year.isPresent()) {
            popular = filmStorage.loadPopularFilms(count, genreId.get(), year.get());
        } else if (genreId.isPresent()) {
            popular = filmStorage.loadPopularFilms(count, genreId.get());
        } else if (year.isPresent()) {
            popular = filmStorage.loadPopularFilms(count, year.get());
        } else {
            popular = filmStorage.loadPopularFilms(count);
        }
        log.debug("Return {} popular films", popular.size());
        return popular;
    }

    public void deleteUser(long id) {
        getFilmOrNotFoundException(id);
        filmStorage.deleteFilm(id);
        log.debug("Delete  movie #{}", id);
    }

    public List<Film> getSortedFilmsOfDirector(long directorId, String sortBy) {
        directorService.getDirectorOrNotFoundException(directorId);
        switch (sortBy) {
            case "YEAR":
                List<Film> films = filmStorage.loadFilmsOfDirectorSortedByYears(directorId);
                log.debug("Return {} films sorted by years", films.size());
                return films;
            case "LIKES":
                films = filmStorage.loadFilmsOfDirectorSortedByRating(directorId);
                log.debug("Return {} films sorted by rating", films.size());
                return films;
            default:
                throw new NotFoundException("Not found sorting property");
        }
    }

    public List<Film> searchFilmByProperty(String query, String filmSearchProperties) {
        List<Film> foundFilmList = filmStorage.searchFilmByProperty(query, filmSearchProperties);
        log.debug("Return film list found by keyword {} and tag {}", query, filmSearchProperties);
        return foundFilmList;
    }
}