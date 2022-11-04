package ru.yandex.practicum.filmorate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Tag(name = "Genres", description = "genres API")
@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(summary = "Get all genres", description = "Get all genres")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Genre.class)))})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> getAll() {
        List<Genre> genres = genreService.getAllGenres();
        log.info("GET {} genres", genres.size());
        return genreService.getAllGenres();
    }

    @Operation(summary = "Get genre by id", description = "Get genres by id")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Genre.class))})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genre get(@PathVariable long id) {
        Genre genre = genreService.getGenreOrNotFoundException(id);
        log.info("GET genre #{}", genre.getId());
        return genre;
    }
}
