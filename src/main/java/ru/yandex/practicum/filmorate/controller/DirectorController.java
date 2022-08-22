package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Director> getAll() {
        List<Director> directors = directorService.getAllDirectors();
        log.info("GET {} directors", directors.size());
        return directors;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Director get(@PathVariable long id) {
        Director director = directorService.getDirectorOrNotFoundException(id);
        log.info("GET director #{}", director.getId());
        return director;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director create(@Valid @RequestBody Director director) {
        Director createdDirector = directorService.createDirector(director);
        log.info("POST director #{}", createdDirector.getId());
        return createdDirector;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Director update(@Valid @RequestBody Director director) {
        Director updatedDirector = directorService.updateDirector(director);
        log.info("PUT director #{}", director.getId());
        return updatedDirector;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        log.info("DELETE director #{}", id);
        directorService.deleteDirector(id);
    }
}
