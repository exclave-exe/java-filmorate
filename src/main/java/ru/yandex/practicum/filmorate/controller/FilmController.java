package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Получен запрос на получение всех фильмов. Количество: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        log.info("Получен запрос на создание фильма: {}", newFilm.getName());

        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Попытка создать фильм с некорректной датой релиза: {}", newFilm.getReleaseDate());
            throw new ValidationException("дата релиза не может быть раньше 28 декабря 1895 года;");
        }

        long newId = getNextId();
        newFilm.setId(newId);
        films.put(newId, newFilm);
        log.info("Фильм успешно создан с ID: {}", newFilm.getId());
        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма с ID: {}", newFilm.getId());

        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Фильм с ID {} успешно обновлен", newFilm.getId());
            return oldFilm;
        }

        log.error("Попытка обновить несуществующий фильм с ID: {}", newFilm.getId());
        throw new NotFoundException("Film не найден");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}