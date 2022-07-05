package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        Film newFilm = filmStorage.add(film);
        log.debug("Create new movie {}", newFilm);
        return newFilm;
    }

    public Film update(Film film) {
        log.debug("Movie before update {}", film);
        Film updatedFilm = get(film.getId());
        if (film.getDescription() == null) film.setDescription(updatedFilm.getDescription());
        if (film.getReleaseDate() == null) film.setReleaseDate(updatedFilm.getReleaseDate());
        if (film.getDuration() == null) film.setDuration(updatedFilm.getDuration());
        if (film.getName() == null || film.getName().isBlank()) film.setName(updatedFilm.getName());
        log.debug("Movie after update {}", film);
        return filmStorage.add(film);
    }

    public ArrayList<Film> getAll() {
        ArrayList<Film> films = filmStorage.getAll();
        log.debug("Load {} movies", films.size());
        return films;
    }

    public Film get(int id) {
        Optional<Film> film = filmStorage.get(id);
        if (film.isPresent()) {
            log.debug("Load from storage movie {}", film);
            return film.get();
        } else {
            log.debug("Movie #{} not found", id);
            throw new NotFoundException("Movie not found");
        }
    }

    public void addScore(int id, int userId) {
        if (hasNotFilmId(id)) throw new NotFoundException("Movie not found");
        if (hasNotUserId(userId)) throw new NotFoundException("User not found");
        log.debug("Creating score for movie #{} from user #{}",  id, userId);
        Set<Integer> likes = getScores(id);
        log.debug("Score before adding is {}", likes.size());
        likes.add(userId);
        log.debug("Score after adding is {}", likes.size());
        filmStorage.saveScores(id, likes);
    }

    public void deleteScore(int id, int userId) {
        if (hasNotFilmId(id)) throw new NotFoundException("Movie not found");
        if (hasNotUserId(userId)) throw new NotFoundException("User not found");
        log.debug("Delete score of movie #{} from user #{}",  id, userId);
        Set<Integer> likes = getScores(id);
        log.debug("Score before deleting is {}", likes.size());
        likes.remove(userId);
        log.debug("Score after deleting is {}", likes.size());
        filmStorage.saveScores(id, likes);
    }

    public List<Film> getPopular(int count) {
        List<Film> popular = getAll().stream()
                .sorted((f1, f2) -> {
                    if (filmStorage.loadScores(f1.getId()).isEmpty() && filmStorage.loadScores(f1.getId()).isEmpty()) {
                        return 0;
                    } else if (filmStorage.loadScores(f2.getId()).isEmpty()) {
                        return -1;
                    } else if (filmStorage.loadScores(f1.getId()).isEmpty()) {
                        return 1;
                    } else {
                        return filmStorage.loadScores(f2.getId()).get().size()
                                - filmStorage.loadScores(f1.getId()).get().size();
                    }
                })
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Return {} popular films", popular.size());
        return popular;
    }

    private Set<Integer> getScores(int id) {
        return filmStorage.loadScores(id).orElseGet(HashSet::new);
    }

    private boolean hasNotFilmId(int id) {
        return filmStorage.get(id).isEmpty();
    }

    private boolean hasNotUserId(int id) {
        return userStorage.get(id).isEmpty();
    }
}
