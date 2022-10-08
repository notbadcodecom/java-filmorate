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

import java.util.List;
import java.util.Optional;

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
    public List<Film> getAll() {
        List<Film> films = filmService.getAllFilms();
        log.info("GET {} films", films.size());
        return films;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film get(@PathVariable long id) {
        Film film = filmService.getFilmOrNotFoundException(id);
        log.info("GET film #{}", film.getId());
        return film;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Validated(Create.class) @RequestBody Film film) {
        Film createdFilm = filmService.create(film);
        log.info("POST film #{}", createdFilm.getId());
        return createdFilm;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Validated(Update.class) @RequestBody Film film) {
        Film updatedFilm = filmService.update(film);
        log.info("PUT film #{}", updatedFilm.getId());
        return updatedFilm;
    }

    @DeleteMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFilm(@PathVariable long filmId){
        log.info("DELETE film #{}", filmId);
        filmService.deleteUser(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void createRatingPoint(@PathVariable long id, @PathVariable long userId) {
        log.info("PUT rating to film #{}", id);
        filmService.addRatingPoint(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRatingPoint(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE rating to film #{}", id);
        filmService.deleteRatingPoint(id, userId);
    }

    @GetMapping("/common")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getUsersCommonFilms(@RequestParam(name = "userId") long userId,
                                          @RequestParam(name = "friendId") long friendId) {
        log.info("GET common films users #{} and #{}", userId, friendId);
        return filmService.getUsersCommonFilms(userId, friendId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopular(
            @RequestParam(required = false, defaultValue = "10") long count,
            @RequestParam Optional<Long> genreId,
            @RequestParam Optional<String> year
    ) {
        log.info("GET {} popular films", count);
        return filmService.getPopular(count, genreId, year);
    }

    @GetMapping("/director/{directorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getByDirectorId(@PathVariable long directorId, @RequestParam String sortBy) {
        log.info("GET films of director sorted by {}", sortBy);
        return filmService.getSortedFilmsOfDirector(directorId, sortBy);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> searchFilmByProperty(@RequestParam(name = "query") String query,
                                           @RequestParam(name = "by", required = false) String filmSearchProperties) {
        log.info("GET searched query {} by {}", query, filmSearchProperties);
        return filmService.searchFilmByProperty(query, filmSearchProperties);
    }
}
