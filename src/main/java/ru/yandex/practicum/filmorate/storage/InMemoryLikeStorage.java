package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class InMemoryLikeStorage implements Like {

    private final Map<Integer, Set<Integer>> likes;

    protected InMemoryLikeStorage() {
        likes = new HashMap<>();
    }

    @Override
    public void saveLikes(int id, Set<Integer> idLikes) {
        likes.put(id, idLikes);
        log.debug("Save likes by id [{}]", id);
    }

    @Override
    public Set<Integer> loadLikes(int id) {
        log.debug("Load likes by id [{}]", id);
        return likes.get(id);
    }
}
