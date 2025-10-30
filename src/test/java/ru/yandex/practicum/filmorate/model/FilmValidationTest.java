package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    // Name проверки
    // Проверка создания Film, в котором поле name корректно
    @Test
    void shouldSuccessValidationWhenNameIsCorrect() {
        Film film = new Film();
        film.setName("test");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    // Проверка создания Film, в котором поле name = null
    @Test
    void shouldFailValidationWhenNameIsNull() {
        Film film = new Film();
        film.setName(null);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле name");
    }

    // Проверка создания Film, в котором поле name = пустая строка
    @Test
    void shouldFailValidationWhenNameIsBlank() {
        Film film = new Film();
        film.setName("");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле name");
    }

    // Description проверки
    // Проверка создания Film, в котором поле description корректно
    @Test
    void shouldSuccessValidationWhenDescriptionLessThan200() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("x".repeat(200));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    // Проверка создания Film, в котором поле description > 200
    @Test
    void shouldFailValidationWhenDescriptionMoreThan200() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("x".repeat(201));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("description", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле description");
    }

    // Duration проверки
    // Проверка создания Film, в котором поле duration корректно
    @Test
    void shouldSuccessValidationWhenDurationIsPositive() {
        Film film = new Film();
        film.setName("test");
        film.setDuration(1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    // Проверка создания Film, в котором поле duration отрицательно
    @Test
    void shouldFailValidationWhenDurationIsNegative() {
        Film film = new Film();
        film.setName("test");
        film.setDuration(-10);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("duration", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле duration");
    }

    // ReleaseDate проверки
    // Проверка создания Film, в котором поле releaseDate корректно
    @Test
    void shouldSuccessValidationWhenReleaseDateIsCorrect() {
        Film film = new Film();
        film.setName("test");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    // Проверка создания Film, в котором поле releaseDate некорректно
    @Test
    void shouldSuccessValidationWhenReleaseDateIsIncorrect() {
        Film film = new Film();
        film.setName("test");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("releaseDate", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле releaseDate");
    }
}