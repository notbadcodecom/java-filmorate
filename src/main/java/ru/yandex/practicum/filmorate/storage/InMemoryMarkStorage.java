package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public abstract class InMemoryMarkStorage {

    private final Map<Integer, Set<Integer>> marks;

    protected InMemoryMarkStorage() {
        marks = new HashMap<>();
    }

    public void saveMarks(int id, Set<Integer> newMarks) {
        marks.put(id, newMarks);
        log.debug("Save for id #{} to memory {} like(s)", id, newMarks.size());
    }

    public Optional<Set<Integer>> loadMarks(int id) {
        int count = (marks.get(id) == null) ? 0 : marks.get(id).size();
        log.debug(
                "Load from memory {} like(s) for id #{}",
                count,
                id
        );
        return Optional.ofNullable(marks.get(id));
    }
}
