package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FriendsService {
    @Qualifier("filmStorage")
    private final Storage<User> storage;

    @Autowired
    public FriendsService(Storage<User> storage) {
        this.storage = storage;
    }

    public void addLike(int id, int friendId) {
        if (hasNotUserId(id) || hasNotUserId(friendId)) throw new NotFoundException("User not found.");

        Set<Integer> likes = getLikesIds(id);
        likes.add(friendId);
        storage.saveLikes(id, likes);
        log.debug("Create like from user [{}] to user [{}] ",  id, friendId);

        likes = getLikesIds(friendId);
        likes.add(id);
        storage.saveLikes(friendId, likes);
        log.debug("Create like from user [{}] to user [{}] ",  friendId, id);
    }

    public void deleteLike(int id, int friendId) {
        if (hasNotUserId(id) || hasNotUserId(friendId)) throw new NotFoundException("User not found.");

        Set<Integer> likes = getLikesIds(id);
        likes.remove(friendId);
        storage.saveLikes(id, likes);
        log.debug("Delete like from user [{}] to user [{}] ",  id, friendId);

        likes = getLikesIds(friendId);
        likes.remove(id);
        storage.saveLikes(friendId, likes);
        log.debug("Delete like from user [{}] to user [{}] ",  friendId, id);
    }

    public List<User> getFriends(int id) {
        return getLikesIds(id).stream()
                .map(storage::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        Set<Integer> common = getLikesIds(id);
        common.retainAll(getLikesIds(otherId));
        log.debug(String.valueOf(common));
        return common.stream()
                .map(storage::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Set<Integer> getLikesIds(int id) {
        return storage.loadLikes(id).orElseGet(HashSet::new);
    }

    private boolean hasNotUserId(int id) {
        return storage.get(id).isEmpty();
    }
}
