package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    @NotNull
    private Long id;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен содержать символ: @")
    private String email;

    @NotBlank(message = "Login не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы")
    private String login;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    private String name;
}