package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmScoreService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final FilmScoreService scoreService;

    @Autowired
    public FilmController(FilmService filmService, FilmScoreService scoreService) {
        this.filmService = filmService;
        this.scoreService = scoreService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<Film> get() {
        log.debug("Request movies.");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film get(@PathVariable int id) {
        log.debug("Request movie [{}]", id);
        return filmService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Validated(Create.class) @RequestBody Film film) {
        log.debug("Request to create movie [{}]", film);
        return filmService.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Validated(Update.class) @RequestBody Film film) {
        log.debug("Request to update movie [{}]", film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createScore(@PathVariable int id, @PathVariable int userId) {
        log.debug("Request to add like for movie [{}], from user [{}],", id, userId);
        scoreService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScore(@PathVariable int id, @PathVariable int userId) {
        log.debug("Request to add like for movie [{}] from friend [{}],", id, userId);
        scoreService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopular(@RequestParam int count) {
        log.debug("Request [{}] popular movie", count);
        return scoreService.getPopular(count);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .peek(e -> log.debug("Validation error [{}]", e.getDefaultMessage()))
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));
    }
}
