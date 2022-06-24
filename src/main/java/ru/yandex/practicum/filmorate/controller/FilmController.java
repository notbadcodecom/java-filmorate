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
        log.debug("Total movies: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film, Errors errors) {
        if (errors.hasErrors()) {
            List<String> messages = errors.getAllErrors().stream()
                    .peek(e -> log.debug("Validation error: {}", e.getDefaultMessage()))
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationException(String.join("; ", messages));
        }
        film.setId(++filmIdCounter);
        films.put(film.getId(), film);
        log.debug("Add movie: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        Film updatedFilm = films.get(film.getId());
        if (updatedFilm == null) {
            String message = "Invalid or no specified film id";
            log.debug("Validation error: " + message);
            throw new ValidationException(message);
        }
        if (film.getDescription() == null) film.setDescription(updatedFilm.getDescription());
        if (film.getReleaseDate() == null) film.setReleaseDate(updatedFilm.getReleaseDate());
        if (film.getDuration() == null) film.setDuration(updatedFilm.getDuration());
        if (film.getName() == null) film.setName(updatedFilm.getName());
        films.put(film.getId(), film);
        log.debug("Update movie: {}", film);
        return film;
    }
}
