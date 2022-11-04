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
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
@Tag(name = "Directors", description = "directors API")
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @Operation(summary = "Get all directors", description = "Get all directors")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Director.class)))})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Director> getAll() {
        List<Director> directors = directorService.getAllDirectors();
        log.info("GET {} directors", directors.size());
        return directors;
    }

    @Operation(summary = "Get director by id", description = "Get director by id or 404 error")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Director get(@PathVariable long id) {
        Director director = directorService.getDirectorOrNotFoundException(id);
        log.info("GET director #{}", director.getId());
        return director;
    }

    @Operation(summary = "Create a new director", description = "Creating a new director")
    @ApiResponse(responseCode = "201", description = "Created",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Director.class))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director create(@Valid @RequestBody Director director) {
        Director createdDirector = directorService.createDirector(director);
        log.info("POST director #{}", createdDirector.getId());
        return createdDirector;
    }

    @Operation(summary = "Update director by id", description = "Update director by id")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))})
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Director update(@Valid @RequestBody Director director) {
        Director updatedDirector = directorService.updateDirector(director);
        log.info("PUT director #{}", director.getId());
        return updatedDirector;
    }

    @Operation(summary = "Delete director by id", description = "Delete director by id")
    @ApiResponse(responseCode = "200", description = "Successful")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        log.info("DELETE director #{}", id);
        directorService.deleteDirector(id);
    }
}
