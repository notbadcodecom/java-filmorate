package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<Film> get() {
        log.debug("GET all films");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film get(@PathVariable int id) {
        log.debug("GET film by id");
        return filmService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Validated(Create.class) @RequestBody Film film) {
        log.debug("POST new film");
        return filmService.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Validated(Update.class) @RequestBody Film film) {
        log.debug("PUT film");
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void createScore(@PathVariable int id, @PathVariable int userId) {
        log.debug("PUT film score");
        filmService.addScore(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteScore(@PathVariable int id, @PathVariable int userId) {
        log.debug("DELETE film score");
        filmService.deleteScore(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopular(@RequestParam(required = false, defaultValue = "10") int count) {
        log.debug("GET popular");
        return filmService.getPopular(count);
    }
}
