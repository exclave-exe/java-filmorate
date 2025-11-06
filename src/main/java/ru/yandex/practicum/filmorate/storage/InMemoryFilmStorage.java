package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    Map<Long, Film> films = new HashMap<>();

    @Override
    public Film getFilm(Long filmId) {
        Film film = films.get(filmId);
        if (film == null) {
            log.error("Фильм не найден filmId={}", filmId);
            throw new NotFoundException("Фильм с ID: " + filmId + " не найден.");
        }
        return film;
    }

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film createFilm(Film newFilm) {
        long newId = getNextId();
        newFilm.setId(newId);
        films.put(newId, newFilm);
        log.info("Фильм создан filmId={}", newId);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (!films.containsKey(newFilm.getId())) {
            log.error("Обновление невозможно — фильм не найден filmId={}", newFilm.getId());
            throw new NotFoundException("Фильм c ID: " + newFilm.getId() + " не найден");
        }

        Film oldFilm = films.get(newFilm.getId());
        oldFilm.setName(newFilm.getName());
        oldFilm.setDescription(newFilm.getDescription());
        oldFilm.setReleaseDate(newFilm.getReleaseDate());
        oldFilm.setDuration(newFilm.getDuration());
        log.debug("Фильм обновлён filmId={}", newFilm.getId());
        return oldFilm;
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
