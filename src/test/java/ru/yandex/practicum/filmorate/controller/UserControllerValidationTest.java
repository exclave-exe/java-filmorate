package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerValidationTest {

    private UserController controller;

    @BeforeEach
    void setUp() {
        controller = new UserController();
    }

    @Test // Проверка создания User, где name = пустая строка (заменяется на login)
    void shouldSetEmailAsNameWhenNameIsBlank() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testUser");
        user.setName(" ");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User created = controller.createUser(user);

        assertEquals("testUser", created.getName(),
                "Пустое имя должно быть заменено на login");
    }

    @Test // Проверка создания User, где name = null (заменяется на login)
    void shouldSetEmailAsNameWhenNameIsNull() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("testUser");
        user.setName(null);
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User created = controller.createUser(user);

        assertEquals("testUser", created.getName(),
                "Пустое имя должно быть заменено на login");
    }
}