package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmScoreService {
    @Qualifier("filmStorage")
    private final Storage<Film> storage;

    private final int MIN_COUNT_OF_POPULAR_FILMS = 10;

    @Autowired
    public FilmScoreService(Storage<Film> storage) {
        this.storage = storage;
    }

    public void addLike(int id, int userId) {
        Set<Integer> likes = getLikesIfPresent(id);
        likes.add(userId);
        storage.saveLikes(id, likes);
        log.debug("Create like to movie [{}] from user [{}]",  id, userId);
    }

    public void deleteLike(int id, int userId) {
        Set<Integer> likes = getLikesIfPresent(id);
        likes.remove(userId);
        storage.saveLikes(id, likes);
        log.debug("Delete like of movie [{}] from user [{}]",  id, userId);
    }

    public List<Film> getPopular(int count) {
        count = (count == 0) ? MIN_COUNT_OF_POPULAR_FILMS : count;
        return storage.getAll().stream()
                .filter(f -> storage.loadLikes(f.getId()).isPresent())
                .sorted((f, o) -> -1 * (
                        storage.loadLikes(f.getId()).get().size() - storage.loadLikes(o.getId()).get().size())
                )
                .limit(count)
                .collect(Collectors.toList());
    }

    private Set<Integer> getLikesIfPresent(int id) {
        Optional<Set<Integer>> likes = storage.loadLikes(id);
        if (likes.isPresent()) return likes.get();
        log.debug("Movie " + id + " not found!");
        throw new NotFoundException("Movie " + id);
    }

}
