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

class UserValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    // Email проверки
    // Проверка на создание User, где поле email корректно
    @Test
    void shouldSuccessValidationWhenEmailCorrect() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testUser");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    // Проверка на создание User, где поле email некорректно
    @Test
    void shouldFailValidationWhenEmailIncorrect() {
        User user = new User();
        user.setEmail("ru.mail@test@mail.ru");
        user.setLogin("testUser");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле email");
    }

    // Проверка на создание User, где поле email = null
    @Test
    void shouldFailValidationWhenEmailIsNull() {
        User user = new User();
        user.setEmail(null);
        user.setLogin("testUser");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле email");
    }

    // Проверка на создание User, где поле email = пустая строка
    @Test
    void shouldFailValidationWhenEmailIsBlank() {
        User user = new User();
        user.setEmail("");
        user.setLogin("testUser");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле email");
    }

    // Login проверки
    // Проверка на создание User, где поле login корректно
    @Test
    void shouldSuccessValidationWhenLoginCorrect() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testUser");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    // Проверка на создание User, где поле login = null
    @Test
    void shouldFailValidationWhenLoginIsNull() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin(null);
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("login", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле login");
    }

    // Проверка на создание User, где поле login = пустая строка
    @Test
    void shouldFailValidationWhenLoginIsBlank() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(2, violations.size(), "Должны быть 2 ошибки");
        for (ConstraintViolation<User> violation : violations) {
            assertEquals("login", violation.getPropertyPath().toString(),
                    "Все ошибки должны быть в поле login");
        }
    }

    // Birthday проверки
    // Проверка на создание User, где поле birthday корректно
    @Test
    void shouldSuccessValidationWhenBirthdayCorrect() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testUser");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    // Проверка на создание User, где поле birthday в будущем
    @Test
    void shouldFailValidationWhenBirthdayInFuture() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testUser");
        user.setBirthday(LocalDate.of(2200, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Должна быть 1 ошибка");
        ConstraintViolation<User> violation = violations.iterator().next();

        assertEquals("birthday", violation.getPropertyPath().toString(),
                "Ошибка должна быть в поле birthday");
    }

    // Name проверки
    // Проверка на создание User, где поле name изначально корректно
    @Test
    void shouldSuccessValidationWhenNameCorrect() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testUser");
        user.setName("test");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
        assertEquals("test", user.getName(), "Поле Name должно инициализироваться");
    }

    // Проверка на создание User, где поле name = пустая строка
    @Test
    void shouldSuccessValidationWhenNameIsBlank() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testUser");
        user.setName(" ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
        assertEquals("testUser", user.getName(), "Поле Name должно заменяться на Login");
    }
}