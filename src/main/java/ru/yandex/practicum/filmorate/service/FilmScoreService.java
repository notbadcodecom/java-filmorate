package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.HashSet;
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
        if (hasNotFilmId(id) || hasNotFilmId(userId)) throw new NotFoundException("Movie not found.");

        Set<Integer> likes = getLikesIds(id);
        likes.add(userId);
        storage.saveLikes(id, likes);
        log.debug("Create like to movie [{}] from user [{}]",  id, userId);
    }

    public void deleteLike(int id, int userId) {
        if (hasNotFilmId(id) || hasNotFilmId(userId)) throw new NotFoundException("Movie not found.");

        Set<Integer> likes = getLikesIds(id);
        likes.remove(userId);
        storage.saveLikes(id, likes);
        log.debug("Delete like of movie [{}] from user [{}]",  id, userId);
    }

    public List<Film> getPopular(Optional<Integer> count) {
        return storage.getAll().stream()
                .sorted((f1, f2) -> {
                    if (storage.loadLikes(f1.getId()).isEmpty()) {
                        return -1;
                    } else if (storage.loadLikes(f2.getId()).isEmpty()) {
                        return 1;
                    } else {
                        return -(
                                storage.loadLikes(f1.getId()).get().size() - storage.loadLikes(f2.getId()).get().size()
                        );
                    }
                }).limit(count.orElse(MIN_COUNT_OF_POPULAR_FILMS))
                .collect(Collectors.toList());
    }

    private Set<Integer> getLikesIds(int id) {
        return storage.loadLikes(id).orElseGet(HashSet::new);
    }

    private boolean hasNotFilmId(int id) {
        return storage.get(id).isEmpty();
    }
}
