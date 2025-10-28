package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    //Name проверки
    @Test
    // Проверка создания Film, в котором поле name корректно
    void shouldSuccessValidationWhenNameIsCorrect() {
        Film film = new Film();
        film.setName("test");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    @Test
        // Проверка создания Film, в котором поле name = null
    void shouldFailValidationWhenNameIsNull() {
        Film film = new Film();
        film.setName(null);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле name");
    }

    @Test
        // Проверка создания Film, в котором поле name = пустая строка
    void shouldFailValidationWhenNameIsBlank() {
        Film film = new Film();
        film.setName("");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals("name", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле name");
    }

    //Description проверки
    @Test
    // Проверка создания Film, в котором поле description корректно
    void shouldSuccessValidationWhenDescriptionLessThan200() {
        Film film = new Film();
        film.setName("test");
        film.setDescription("x".repeat(200));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    @Test
        // Проверка создания Film, в котором поле description > 200
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
    @Test
    // Проверка создания Film, в котором поле duration корректно
    void shouldSuccessValidationWhenDurationIsPositive() {
        Film film = new Film();
        film.setName("test");
        film.setDuration(1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    @Test
        // Проверка создания Film, в котором поле duration отрицательно
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
}