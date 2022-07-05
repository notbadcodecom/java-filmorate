package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
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
        log.debug("Create movie [{}]", film);
        return storage.add(film);
    }

    public Film update(Film film) {
        Film updatedFilm = storage.get(film.getId()).get();
        if (film.getDescription() == null) film.setDescription(updatedFilm.getDescription());
        if (film.getReleaseDate() == null) film.setReleaseDate(updatedFilm.getReleaseDate());
        if (film.getDuration() == null) film.setDuration(updatedFilm.getDuration());
        if (film.getName() == null || film.getName().isBlank()) film.setName(updatedFilm.getName());
        log.debug("Update movie [{}]", film);
        return storage.add(film);
    }

    public ArrayList<Film> getAll() {
        log.debug("Return {} movies.", storage.getAll().size());
        return storage.getAll();
    }

}
