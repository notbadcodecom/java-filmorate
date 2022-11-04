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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Tag(name = "MPA", description = "MPA rating API")
@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @Operation(summary = "Get MPA ratings titles", description = "Get ratings titles of Motion Picture Association")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Mpa.class)))})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Mpa> getAll() {
        List<Mpa> mpa = mpaService.getAllMpa();
        log.info("GET {} mpa", mpa.size());
        return mpa;
    }

    @Operation(summary = "Get MPA title", description = "Get MPA rating title by id")
    @ApiResponse(responseCode = "200", description = "Successful",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Mpa.class))})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa get(@PathVariable long id) {
        Mpa mpa = mpaService.getMpaOrNotFoundException(id);
        log.info("GET {} mpa", mpa.getId());
        return mpa;
    }
}
