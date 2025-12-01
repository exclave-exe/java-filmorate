package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Запрошен список фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info("Запрошен фильм id={}", id);
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        log.info("Запрошено создание фильма: {}", newFilm.getName());
        return filmService.createFilm(newFilm);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("Запрошено обновление фильма filmId={}", newFilm.getId());
        return filmService.updateFilm(newFilm);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") long count) {
        log.info("Запрошены популярные фильмы count={}", count);
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable @Positive long id,
                        @PathVariable @Positive long userId) {
        log.info("Запрошено добавление лайка userId={} filmId={}", userId, id);
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable @Positive long id,
                           @PathVariable @Positive long userId) {
        log.info("Запрошено удаление лайка userId={} filmId={}", userId, id);
        filmService.deleteLike(userId, id);
    }
}
