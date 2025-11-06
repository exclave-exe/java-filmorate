package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Collection<Film> getFilms() {
        return filmStorage.getFilms().values();
    }

    public Film createFilm(@Valid @RequestBody Film newFilm) {
        return filmStorage.createFilm(newFilm);
    }

    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public Collection<Film> getPopularFilms(long count) {
        Collection<Film> popularFilms = filmStorage.getFilms().values().stream()
                .sorted((f1, f2) -> Integer.compare(
                        f2.getUserIdsWhoLiked().size(),
                        f1.getUserIdsWhoLiked().size()))
                .limit(count)
                .toList();
        log.debug("Найдено популярных фильмов: {} (запрошено: {})", popularFilms.size(), count);
        return popularFilms;
    }

    public void deleteLike(long userId, long filmId) {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        boolean removed = film.getUserIdsWhoLiked().remove(user.getId());
        if (removed) {
            log.info("Лайк удалён userId={} filmId={}", userId, filmId);
        } else {
            log.debug("Лайк не был удалён поскольку отсутствовал userId={} filmId={}", userId, filmId);
        }
    }

    public void addLike(long userId, long filmId) {
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId);
        boolean added = film.getUserIdsWhoLiked().add(user.getId());
        if (added) {
            log.info("Лайк добавлен userId={} filmId={}", userId, filmId);
        } else {
            log.debug("Лайк не был добавлен поскольку уже присутствовал userId={} filmId={}", userId, filmId);
        }
    }
}
