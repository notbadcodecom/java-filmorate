package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        Set<Integer> likes = getLikesIfPresent(id);
        likes.add(friendId);
        storage.saveLikes(id, likes);
        log.debug("Create like from user [{}] to user [{}] ",  id, friendId);

        likes = getLikesIfPresent(friendId);
        likes.add(id);
        storage.saveLikes(friendId, likes);
        log.debug("Create like from user [{}] to user [{}] ",  friendId, id);
    }

    public void deleteLike(int id, int friendId) {
        Set<Integer> likes = getLikesIfPresent(id);
        likes.remove(friendId);
        storage.saveLikes(id, likes);
        log.debug("Delete like from user [{}] to user [{}] ",  id, friendId);

        likes = getLikesIfPresent(friendId);
        likes.remove(id);
        storage.saveLikes(friendId, likes);
        log.debug("Delete like from user [{}] to user [{}] ",  friendId, id);
    }

    public List<User> getFriends(int id) {
        return getLikesIfPresent(id).stream()
                .map(storage::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        Set<Integer> common = getLikesIfPresent(id);
        common.retainAll(getLikesIfPresent(otherId));
        return common.stream()
                .map(storage::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

    }

    private Set<Integer> getLikesIfPresent(int id) {
        Optional<Set<Integer>> likes = storage.loadLikes(id);
        if (likes.isPresent()) return likes.get();
        log.debug("User " + id + " not found!");
        throw new NotFoundException("User " + id);
    }

}
