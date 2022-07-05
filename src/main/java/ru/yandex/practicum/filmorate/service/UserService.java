package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    @Qualifier("userStorage")
    private final Storage<User> storage;

    @Autowired
    public UserService(Storage<User> storage) {
        this.storage = storage;
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("Create user [{}]", user);
        return storage.add(user);
    }

    public User update(User user) {
        User updatedUser = storage.get(user.getId()).get();
        if (user.getBirthday() == null) user.setBirthday(updatedUser.getBirthday());
        if (user.getLogin() == null) user.setLogin(updatedUser.getLogin());
        if (user.getEmail() == null) user.setEmail(updatedUser.getEmail());
        log.debug("Update user [{}]", user);
        return storage.add(user);
    }

    public ArrayList<User> getAll() {
        log.debug("Return {} users.", storage.getAll().size());
        return storage.getAll();
    }

    public void addLikes(int acceptorId, int giverId) {
        if (acceptorId != giverId) {
            Set<Integer> acceptorLikes = storage.loadLikes(acceptorId);
            Set<Integer> giverLikes = storage.loadLikes(giverId);

            acceptorLikes.add(giverId);
            giverLikes.add(acceptorId);

            storage.saveLikes(acceptorId, acceptorLikes);
            storage.saveLikes(giverId, giverLikes);

            log.debug("Add likes to users [{}, {}]", acceptorId, giverId);
        }
    }

    public void deleteLikes(int acceptorId, int giverId) {
        Set<Integer> acceptorLikes = storage.loadLikes(acceptorId);
        Set<Integer> giverLikes = storage.loadLikes(giverId);

        acceptorLikes.remove(giverId);
        giverLikes.remove(acceptorId);

        storage.saveLikes(acceptorId, acceptorLikes);
        storage.saveLikes(giverId, giverLikes);

        log.debug("Remove likes from users [{}, {}]", acceptorId, giverId);
    }
}
