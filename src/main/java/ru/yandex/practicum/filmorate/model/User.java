package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;

/**
 * User entity.
 */

@Data
public class User {
    private Long id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен содержать символ: @")
    private String email;

    @NotBlank(message = "Login не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы")
    private String login;

    private String name; // Валидация в UserController

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}