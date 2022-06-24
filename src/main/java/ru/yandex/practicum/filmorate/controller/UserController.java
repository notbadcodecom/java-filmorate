package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int userIdCounter = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @GetMapping
    public List<User> getUsers() {
        log.info("Total users: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors() || emails.contains(user.getEmail())) {
            List<String> messages = errors.getAllErrors().stream()
                    .peek(e -> log.debug("Validation error: {}", e.getDefaultMessage()))
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            if (emails.contains(user.getEmail())) {
                String message = "Email already in use";
                log.debug("Validation error: " + message);
                messages.add(message);
            }
            throw new ValidationException(String.join("; ", messages) + ".");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++userIdCounter);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        log.debug("Add user: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        User updatedUser = users.get(user.getId());
        if (updatedUser == null) {
            String message = "Invalid or no specified user id";
            log.debug("Validation error: " + message);
            throw new ValidationException(message);
        }
        if (emails.contains(user.getEmail()) && !user.getEmail().equals(updatedUser.getEmail())) {
            String message = "Email already in use";
            log.debug("Validation error: " + message);
            throw new ValidationException(message);
        }
        if (user.getBirthday() == null) user.setBirthday(updatedUser.getBirthday());
        if (user.getLogin() == null) user.setLogin(updatedUser.getLogin());
        if (user.getEmail() == null) user.setEmail(updatedUser.getEmail());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        log.debug("Update user: {}", user);
        return user;
    }
}
