package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private long filmIdCounter;
    private final Map<Long, Film> films;
    private final Map<Long, Set<Long>> scores;

    public InMemoryFilmStorage() {
        filmIdCounter = 0L;
        films = new HashMap<>();
        scores = new HashMap<>();
    }

    @Override
    public Optional<Film> loadFilm(long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public long saveFilm(Film film) {
        if (film.getId() == 0) film.setId(++filmIdCounter);
        films.put(film.getId(), film);
        return film.getId();
    }

    @Override
    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public ArrayList<Film> loadFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void saveRatingPoint(long filmId, long userId) {
        Set<Long> filmScores = scores.get(filmId);
        filmScores.add(userId);
        scores.put(filmId, filmScores);
    }

    @Override
    public void deleteRatingPoint(long filmId, long userId) {
        Set<Long> filmScores = scores.get(filmId);
        filmScores.remove(userId);
        scores.put(filmId, filmScores);
    }

    @Override
    public boolean hasFilmRatingFromUser(long filmId, long userId) {
        Optional<Set<Long>> filmScores =  Optional.ofNullable(scores.get(filmId));
        return filmScores.map(l -> l.contains(userId)).orElse(false);
    }

    @Override
    public List<Film> loadPopularFilms(long count) {
        return new ArrayList<>(films.values()).stream()
                .sorted((f1, f2) -> {
                    if (loadScores(f1.getId()) == 0 && loadScores(f1.getId()) == 0) {
                        return 0;
                    } else if (loadScores(f2.getId()) == 0) {
                        return -1;
                    } else if (loadScores(f1.getId()) == 0) {
                        return 1;
                    } else {
                        return loadScores(f2.getId()) - loadScores(f1.getId());
                    }
                })
                .limit(count)
                .collect(Collectors.toList());
    }

    private int loadScores(long filmId) {
        return scores.get(filmId).size();
    }
}
