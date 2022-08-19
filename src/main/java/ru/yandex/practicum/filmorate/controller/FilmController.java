package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.List;
import java.util.Optional;

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
    public List<Film> get() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film get(@PathVariable long id) {
        return filmService.getFilmOrNotFoundException(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Validated(Create.class) @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Validated(Update.class) @RequestBody Film film) {
        return filmService.update(film);
    }

    @DeleteMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFilm(@PathVariable long filmId){
        filmService.deleteUser(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void createRatingPoint(@PathVariable long id, @PathVariable long userId) {
        filmService.addRatingPoint(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRatingPoint(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteRatingPoint(id, userId);
    }

    @GetMapping("/common")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getUsersCommonFilms(@RequestParam(name = "userId") long userId,
                                          @RequestParam(name = "friendId") long friendId) {
        return filmService.getUsersCommonFilms(userId, friendId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopular(
            @RequestParam(required = false, defaultValue = "10") long count,
            @RequestParam Optional<Long> genreId,
            @RequestParam Optional<String> year
    ) {
        return filmService.getPopular(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getByDirectorId(@PathVariable long directorId, @RequestParam String sortBy) {
        return filmService.getSortedFilmsOfDirector(directorId, sortBy.toUpperCase());
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> searchFilmByProperty(@RequestParam(name = "query") String query,
                                           @RequestParam(name = "by", required = false) String filmSearchProperties) {
        return filmService.searchFilmByProperty(query, filmSearchProperties);
    }
}
