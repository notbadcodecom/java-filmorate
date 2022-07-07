package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FriendService friendService;

    @Autowired
    public UserController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> get() {
        log.debug("Request users.");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User get(@PathVariable int id) {
        log.debug("Request user [{}]", id);
        return userService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Validated(Create.class) @RequestBody User user) {
        log.debug("Request to create user [{}]", user);
        return userService.create(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Validated(Update.class) @RequestBody User user) {
        log.debug("Request to update user [{}]", user);
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void createFriendship(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Request to add like for user [{}] from friend [{}]", id, friendId);
        friendService.addLikes(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriendship(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Request to add like for user [{}] from friend [{}]", id, friendId);
        friendService.deleteLikes(id, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable int id) {
        log.debug("Request friends of user [{}]", id);
        return friendService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Request common friends of user [{}]", id);
        return friendService.getCommonFriends(id, otherId);
    }
}
