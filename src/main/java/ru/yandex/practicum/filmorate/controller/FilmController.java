package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int filmIdCounter = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        log.debug("Get movies. Total movies: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping("/add")
    public Film addFilm(@Valid @RequestBody Film film, Errors errors) {
        if (errors.hasErrors()) {
            List<String> messages = errors.getAllErrors().stream()
                    .peek(e -> log.debug(e.getDefaultMessage()))
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationException(String.join("; ", messages) + ".");
        }
        film.setId(++filmIdCounter);
        films.put(film.getId(), film);
        log.debug("Add movie: {}", film);
        return film;
    }

    @PutMapping("/update")
    public Film updateFilm(@Valid @RequestBody Film film, Errors errors) {
        if (errors.hasErrors() || films.get(film.getId()) == null) {
            List<String> messages = errors.getAllErrors().stream()
                    .peek(e -> log.debug(e.getDefaultMessage()))
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            if (films.get(film.getId()) == null) {
                String message = "Invalid or no specified id";
                log.debug(message);
                messages.add(message);
            }
            throw new ValidationException(String.join("; ", messages) + ".");
        }
        films.put(film.getId(), film);
        log.debug("Update movie: {}", film);
        return film;
    }
}
