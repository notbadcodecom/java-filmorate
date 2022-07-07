package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class InMemoryLikeStorage implements LikeStorage {

    private final Map<Integer, Set<Integer>> likes;

    protected InMemoryLikeStorage() {
        likes = new HashMap<>();
    }

    @Override
    public void saveLikes(int id, Set<Integer> idLikes) {
        likes.put(id, idLikes);
        log.debug("Save for id #{} to memory {} like(s)", id, idLikes.size());
    }

    @Override
    public Optional<Set<Integer>> loadLikes(int id) {
        int count = (likes.get(id) == null) ? 0 : likes.get(id).size();
        log.debug(
                "Load from memory {} like(s) for id #{}",
                count,
                id
        );
        return Optional.ofNullable(likes.get(id));
    }
}
