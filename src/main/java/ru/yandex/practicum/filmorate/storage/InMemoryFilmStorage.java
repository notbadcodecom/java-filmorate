package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component("filmStorage")
public class InMemoryFilmStorage implements Storage<Film> {

    private int filmIdCounter = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Optional<Film> get(int id) {
        log.debug("Getting movie [{}]", films.get(id));
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public Film add(Film film) {
        film.setId(++filmIdCounter);
        films.put(film.getId(), film);
        log.debug("Add movie [{}]", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Film updatedFilm = films.get(film.getId());
        if (film.getDescription() == null) film.setDescription(updatedFilm.getDescription());
        if (film.getReleaseDate() == null) film.setReleaseDate(updatedFilm.getReleaseDate());
        if (film.getDuration() == null) film.setDuration(updatedFilm.getDuration());
        if (film.getName() == null || film.getName().isBlank()) film.setName(updatedFilm.getName());
        films.put(film.getId(), film);
        log.debug("Update movie [{}]", film);
        return film;
    }

    @Override
    public ArrayList<Film> getAll() {
        log.debug("Return {} movies.", films.size());
        return new ArrayList<>(films.values());
    }
}
