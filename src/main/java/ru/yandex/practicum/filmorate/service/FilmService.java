package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;

@Slf4j
@Service
public class FilmService {
    @Qualifier("filmStorage")
    private final Storage<Film> storage;

    @Autowired
    public FilmService(Storage<Film> storage) {
        this.storage = storage;
    }

    public Film create(Film film) {
        Film newFilm = storage.add(film);
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
        return storage.add(film);
    }

    public ArrayList<Film> getAll() {
        ArrayList<Film> films = storage.getAll();
        log.debug("Load {} movies", films.size());
        log.debug("List of movies: {}", films);
        return films;
    }

    public Film get(int id) {
        Optional<Film> film = storage.get(id);
        if (film.isPresent()) {
            log.debug("Load from storage movie {}", film);
            return film.get();
        } else {
            log.debug("Movie #{} not found", id);
            throw new NotFoundException("Movie not found");
        }
    }
}
