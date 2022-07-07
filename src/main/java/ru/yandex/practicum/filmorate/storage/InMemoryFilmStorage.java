package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component("filmStorage")
public class InMemoryFilmStorage extends InMemoryLikeStorageStorage implements Storage<Film> {

    private int filmIdCounter;
    private final Map<Integer, Film> films;

    public InMemoryFilmStorage() {
        filmIdCounter = 0;
        films = new HashMap<>();
    }

    @Override
    public Optional<Film> get(int id) {
        log.debug("Getting from memory movie {}", films.get(id));
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film add(Film film) {
        if (film.getId() == 0) film.setId(++filmIdCounter);
        log.debug("Generate ID for movie {}", film);
        films.put(film.getId(), film);
        log.debug("Save to memory movie {}", film);
        return film;
    }

    @Override
    public ArrayList<Film> getAll() {
        log.debug("Getting all ({}) movies.", films.size());
        return new ArrayList<>(films.values());
    }
}
