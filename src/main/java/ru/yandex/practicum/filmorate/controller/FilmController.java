package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmSortingProperties;
import ru.yandex.practicum.filmorate.storage.FilmSearchBy;
import ru.yandex.practicum.filmorate.validation.Create;
import ru.yandex.practicum.filmorate.validation.Update;

import java.util.List;
import java.util.Optional;

@Tag(name = "Films", description = "films API")
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @Operation(summary = "Get all films", description = "Get all films")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Film.class)))})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAll() {
        List<Film> films = filmService.getAllFilms();
        log.info("GET {} films", films.size());
        return films;
    }

    @Operation(summary = "Get film by id", description = "Get film by id or 404 error")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Film.class))})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film get(@PathVariable long id) {
        Film film = filmService.getFilmOrNotFoundException(id);
        log.info("GET film #{}", film.getId());
        return film;
    }

    @Operation(summary = "Add film", description = "Add film")
    @ApiResponse(responseCode = "201", description = "Created",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Film.class))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Validated(Create.class) @RequestBody Film film) {
        Film createdFilm = filmService.create(film);
        log.info("POST film #{}", createdFilm.getId());
        return createdFilm;
    }

    @Operation(summary = "Update film", description = "Update film by id or 404 error")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Film.class))})
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film update(@Validated(Update.class) @RequestBody Film film) {
        Film updatedFilm = filmService.update(film);
        log.info("PUT film #{}", updatedFilm.getId());
        return updatedFilm;
    }

    @Operation(summary = "Delete film", description = "Delete film by id")
    @ApiResponse(responseCode = "200", description = "Successful")
    @DeleteMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFilm(@PathVariable long filmId){
        log.info("DELETE film #{}", filmId);
        filmService.deleteUser(filmId);
    }

    @Operation(summary = "Add rating point to film", description = "Add rating point to film by id")
    @ApiResponse(responseCode = "200", description = "Successful")
    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void createRatingPoint(@PathVariable long id, @PathVariable long userId) {
        log.info("PUT rating to film #{}", id);
        filmService.addRatingPoint(id, userId);
    }

    @Operation(summary = "Delete rating point of film", description = "Delete rating point of film")
    @ApiResponse(responseCode = "204", description = "No content")
    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRatingPoint(@PathVariable long id, @PathVariable long userId) {
        log.info("DELETE rating to film #{}", id);
        filmService.deleteRatingPoint(id, userId);
    }

    @Operation(summary = "Get common films of users", description = "Get common films of two users")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Film.class)))})
    @GetMapping("/common")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getUsersCommonFilms(@RequestParam(name = "userId") long userId,
                                          @RequestParam(name = "friendId") long friendId) {
        log.info("GET common films users #{} and #{}", userId, friendId);
        return filmService.getUsersCommonFilms(userId, friendId);
    }

    @Operation(summary = "Get popular films",
            description = "Get popular films. You can set the required number by \"count\" in query")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Film.class)))})
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

    @Operation(summary = "Get films of director", description = "Get films of director by id. " +
            "Can be sorted by year or likes.")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Film.class)))})
    @GetMapping("/director/{directorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getByDirectorId(
            @PathVariable long directorId,
            @Parameter(schema = @Schema(implementation = FilmSortingProperties.class))
            @RequestParam String sortBy
    ) {
        log.info("GET films of director sorted by {}", sortBy);
        return filmService.getSortedFilmsOfDirector(directorId, sortBy);
    }

    @Operation(summary = "Search films by property", description = "Search films by properties: director or film.")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Film.class)))})
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> searchFilmByProperty(
            @RequestParam(name = "query") String query,
            @Parameter(schema = @Schema(implementation = FilmSearchBy.class))
            @RequestParam(name = "by", required = false) String filmSearchProperties
    ) {
        log.info("GET searched query {} by {}", query, filmSearchProperties);
        return filmService.searchFilmByProperty(query, filmSearchProperties);
    }
}
