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

    //Email проверки
    @Test // Проверка на создание User, где поле email корректно
    void shouldSuccessValidationWhenCorrectEmail() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testUser");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    @Test // Проверка на создание User, где поле email некорректно
    void shouldFailValidationWhenIncorrectEmail() {
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

    @Test // Проверка на создание User, где поле email = null
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

    @Test // Проверка на создание User, где поле email = пустая строка
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

    //Login проверки
    @Test // Проверка на создание User, где поле login корректно
    void shouldSuccessValidationWhenCorrectLogin() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testUser");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    @Test // Проверка на создание User, где поле login = null
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

    @Test // Проверка на создание User, где поле login = пустая строка
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

    //birthday проверки
    @Test // Проверка на создание User, где поле birthday корректно
    void shouldSuccessValidationWhenCorrectBirthday() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testUser");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Не должно быть ошибок валидации");
    }

    @Test // Проверка на создание User, где поле birthday в будущем
    void shouldFailValidationWhenBirthdayInFea() {
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
}