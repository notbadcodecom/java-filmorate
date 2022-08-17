package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DirectorService {
    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public Director getDirectorOrNotFoundException(long id) {
        Optional<Director> director = directorStorage.loadDirector(id);
        if (director.isPresent()) {
            log.debug("Load {}", director.get());
            return director.get();
        } else {
            throw new NotFoundException("Director #" + id + " not found");
        }
    }

    public List<Director> getFilmDirectorsById(long id) {
        return directorStorage.loadDirectorsByFilmId(id);
    }

    public Director createDirector(Director director) {
        long id = directorStorage.saveDirector(director);
        return getDirectorOrNotFoundException(id);
    }

    public Director updateDirector(Director director) {
        Director loadedDirector = getDirectorOrNotFoundException(director.getId());
        loadedDirector.setName(director.getName());
        directorStorage.updateDirector(loadedDirector);
        return getDirectorOrNotFoundException(director.getId());
    }

    public void deleteDirector(long id) {
        directorStorage.deleteDirector(id);
    }

    public void addFilmDirectors(long id, List<Director> directors) {
        directorStorage.saveDirectorsToFilm(id, directors);
    }

    public void updateFilmDirectors(long id, List<Director> directors) {
        directorStorage.deleteDirectorsOfFilm(id);
        directorStorage.saveDirectorsToFilm(id, directors);
    }

    public void deleteFilmDirectors(long id) {
        directorStorage.deleteDirectorsOfFilm(id);
    }

    public List<Director> getAllDirectors() {
        List<Director> directors = directorStorage.getAllDirectors();
        log.debug("Load {} directors", directors.size());
        return directors;
    }
}
