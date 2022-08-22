package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Mpa> getAll() {
        List<Mpa> mpa = mpaService.getAllMpa();
        log.info("GET {} mpa", mpa.size());
        return mpa;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa get(@PathVariable long id) {
        Mpa mpa = mpaService.getMpaOrNotFoundException(id);
        log.info("GET {} mpa", mpa.getId());
        return mpa;
    }
}
