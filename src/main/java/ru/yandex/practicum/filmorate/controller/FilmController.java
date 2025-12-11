package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.FilmUpdateDto;
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
    public Collection<FilmResponseDto> getFilms() {
        log.info("Запрошен список фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public FilmResponseDto getFilmById(@PathVariable @Positive long id) {
        log.info("Запрошен фильм id={}", id);
        return filmService.getFilmById(id);
    }

    @PostMapping
    public FilmResponseDto createFilm(@Valid @RequestBody FilmCreateDto filmCreateDto) {
        log.info("Запрошено создание фильма: {}", filmCreateDto.getName());
        return filmService.createFilm(filmCreateDto);
    }

    @PutMapping
    public FilmResponseDto updateFilm(@Valid @RequestBody FilmUpdateDto filmUpdateDto) {
        log.info("Запрошено обновление фильма filmId={}", filmUpdateDto.getId());
        return filmService.updateFilm(filmUpdateDto);
    }

    @GetMapping("/popular")
    public Collection<FilmResponseDto> getPopularFilms(@RequestParam(defaultValue = "10") @Positive long count) {
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
