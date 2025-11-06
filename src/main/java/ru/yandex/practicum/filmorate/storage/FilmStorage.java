package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    public Film getFilm(Long filmId);

    public Map<Long, Film> getFilms();

    public Film createFilm(Film newFilm);

    public Film updateFilm(Film newFilm);
}