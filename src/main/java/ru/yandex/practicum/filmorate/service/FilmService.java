package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    public Collection<Film> getFilms() {
        return filmRepository.getFilms();
    }

    public Film getFilmById(long id) {
        return validateFilmExists(id);
    }

    @Transactional
    public Film createFilm(Film film) {
        validateFilmRefs(film);
        Long id = filmRepository.createFilm(film);
        return validateFilmExists(id);
    }

    @Transactional
    public Film updateFilm(Film film) {
        validateFilmExists(film.getId());
        validateFilmRefs(film);
        filmRepository.updateFilm(film);
        return validateFilmExists(film.getId());
    }

    public Collection<Film> getPopularFilms(long count) {
        return filmRepository.getPopularFilms(count);
    }

    @Transactional
    public void addLike(long userId, long filmId) {
        validateFilmExists(filmId);
        validateUserExists(userId);
        filmRepository.addLike(filmId, userId);
    }

    @Transactional
    public void deleteLike(long userId, long filmId) {
        validateFilmExists(filmId);
        validateUserExists(userId);
        filmRepository.deleteLike(filmId, userId);
    }

    private void validateFilmRefs(Film film) {
        Mpa mpa = film.getMpa();
        if (mpa == null || mpa.getId() == null ||
                mpaRepository.getMpaById(mpa.getId()).isEmpty()) {
            throw new NotFoundException("MPA с id=" + (mpa != null ? mpa.getId() : null) + " не найден");
        }

        Set<Genre> genres = film.getGenres();
        if (genres == null || genres.isEmpty()) {
            return;
        }

        Set<Integer> ids = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        for (Integer id : ids) {
            if (genreRepository.getGenreById(id).isEmpty()) {
                throw new NotFoundException("Жанр с id=" + id + " не найден");
            }
        }
    }

    private User validateUserExists(long userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    private Film validateFilmExists(long filmId) {
        return filmRepository.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + filmId + " не найден"));
    }
}
