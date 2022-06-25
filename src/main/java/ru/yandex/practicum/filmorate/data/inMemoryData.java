package ru.yandex.practicum.filmorate.data;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public class inMemoryData {
    private static final inMemoryData instance = new inMemoryData();
    private static int filmIdCounter = 0;
    private static int userIdCounter = 0;
    private static final Map<Integer, Film> films = new HashMap<>();
    private static final Map<Integer, User> users = new HashMap<>();
    private static final Set<String> emails = new HashSet<>();

    public static inMemoryData getInstance() {
        return instance;
    }

    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }

    public Film getFilm(int id) {
        return films.get(id);
    }

    public ArrayList<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public int getFilmId() {
        return ++filmIdCounter;
    }

    public boolean containsEmail(String email) {
        return emails.contains(email);
    }

    public void removeEmail(String email) {
        emails.remove(email);
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
        emails.add(user.getEmail());
    }

    public User getUser(int id) {
        return users.get(id);
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public int getUserId() {
        return ++userIdCounter;
    }
}
