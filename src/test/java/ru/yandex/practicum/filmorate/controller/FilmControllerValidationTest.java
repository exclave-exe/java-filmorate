package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerValidationTest {

    private FilmController controller;

    @BeforeEach
    void setUp() {
        controller = new FilmController();
    }

    @Test // Проверка создания Film с корректными данными
    void shouldCreateFilmWithValidData() {
        Film film = createValidFilm();

        Film created = controller.createFilm(film);

        assertNotNull(created.getId(), "ID должен быть установлен");
        assertEquals(1L, created.getId(), "Первый фильм должен иметь ID = 1");
        assertEquals("Test Film", created.getName());
        assertEquals("Test Description", created.getDescription());
        assertEquals(LocalDate.of(2000, 1, 1), created.getReleaseDate());
        assertEquals(120, created.getDuration());
    }

    @Test // Проверка создания Film с датой релиза = 28.12.1895
    void shouldCreateFilmWhenReleaseDateIsMinimum() {
        Film film = createValidFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        Film created = controller.createFilm(film);

        assertNotNull(created.getId());
        assertEquals(LocalDate.of(1895, 12, 28), created.getReleaseDate());
    }

    @Test // Проверка создания Film с датой релиза раньше 28.12.1895
    void shouldThrowExceptionWhenReleaseDateIsTooEarly() {
        Film film = createValidFilm();
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            controller.createFilm(film);
        }, "Должно выбросить ValidationException");

        assertTrue(exception.getMessage().contains("1895"),
                "Сообщение об ошибке должно содержать год 1895");
    }

    private Film createValidFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        return film;
    }
}