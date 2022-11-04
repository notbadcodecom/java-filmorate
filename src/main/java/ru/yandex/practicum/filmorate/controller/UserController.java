package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.RecommendationService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.*;

@Tag(name = "Users", description = "users API")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RecommendationService recommendationService;

    @Autowired
    public UserController(UserService userService, RecommendationService recommendationService) {
        this.userService = userService;
        this.recommendationService = recommendationService;
    }

    @Operation(summary = "Get all users", description = "Get all users")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by id", description = "Get user by id or 404 error")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User get(@PathVariable long id) {
        return userService.getUserOrNotFoundException(id);
    }

    @Operation(summary = "Create a new user", description = "Creating a new user")
    @ApiResponse(responseCode = "201", description = "Create",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Validated(Create.class) @RequestBody User user) {
        return userService.create(user);
    }

    @Operation(summary = "Update user", description = "Update user or 404 error")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))})
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Validated(Update.class) @RequestBody User user) {
        return userService.update(user);
    }

    @Operation(summary = "Delete user by id", description = "Delete user by id")
    @ApiResponse(responseCode = "200", description = "Successful")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }

    @Operation(summary = "Creating friend request", description = "Creating friendship request (subscription)")
    @ApiResponse(responseCode = "200", description = "Successful")
    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void createFriendship(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriendship(id, friendId);
    }

    @Operation(summary = "Confirm friendship request", description = "Confirm friendship request (subscription)")
    @ApiResponse(responseCode = "200", description = "Successful")
    @PutMapping("/{id}/friends/{friendId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void confirmFriendship(@PathVariable long id, @PathVariable long friendId) {
        userService.confirmFriendship(id, friendId);
    }

    @Operation(summary = "Refuse friendship request", description = "Refuse friendship request (subscription)")
    @ApiResponse(responseCode = "204", description = "No content")
    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refuseFriendship(@PathVariable long id, @PathVariable long friendId) {
        userService.refuseFriendship(id, friendId);
    }

    @Operation(summary = "Get events feed of user", description = "Get events feed of user by id")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Event.class)))})
    @GetMapping("/{id}/feed")
    @ResponseStatus(HttpStatus.OK)
    public List<Event> getEvents(@PathVariable long id) {
        return userService.getEvents(id);
    }

    @Operation(summary = "Get friends list of user", description = "Get friends list of user by id")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable long id) {
        return userService.getFriends(id);
    }

    @Operation(summary = "Get common friends of users", description = "Get common friends of two users by ids")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @Operation(summary = "Find recommended movies",
            description = "Find recommended movies based on other users ratings")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = User.class)))})
    @GetMapping("/{id}/recommendations")
    ResponseEntity<?> findRecommendationsById(@PathVariable int id) {
        return ResponseEntity.ok(recommendationService.findRecommendedFilms(id));
    }
}
