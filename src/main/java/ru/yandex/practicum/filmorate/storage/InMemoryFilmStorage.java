package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component("filmStorage")
public class InMemoryFilmStorage implements Storage<Film> {

    private int filmIdCounter;
    private final Map<Integer, Film> films;
    private final Map<Integer, Set<Integer>> likes;

    public InMemoryFilmStorage() {
        filmIdCounter = 0;
        films = new HashMap<>();
        likes = new HashMap<>();
    }


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

    @Override
    public void addLike(int acceptorId, int giverId) {
        Set<Integer> filmLikes = likes.get(acceptorId);
        filmLikes.add(giverId);
        likes.put(acceptorId, filmLikes);
        log.debug("User [{}] Add like to film [{}]", giverId, acceptorId);
    }

    @Override
    public void removeLike(int acceptorId, int giverId) {
        Set<Integer> filmLikes = likes.get(acceptorId);
        filmLikes.remove(giverId);
        likes.put(acceptorId, filmLikes);
        log.debug("User [{}] remove like from film [{}]", giverId, acceptorId);
    }
}
