package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component("filmStorage")
public class InMemoryFilmStorage extends InMemoryLikeStorage implements Storage<Film> {

    private int filmIdCounter;
    private final Map<Integer, Film> films;

    public InMemoryFilmStorage() {
        filmIdCounter = 0;
        films = new HashMap<>();
    }

    @Override
    public Optional<Film> get(int id) {
        log.debug("Getting movie [{}]", films.get(id));
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film add(Film film) {
        if (film.getId() == 0) film.setId(++filmIdCounter);
        films.put(film.getId(), film);
        log.debug("Save movie [{}]", film);
        return film;
    }

    @Override
    public ArrayList<Film> getAll() {
        log.debug("Getting {} movies.", films.size());
        return new ArrayList<>(films.values());
    }
}
