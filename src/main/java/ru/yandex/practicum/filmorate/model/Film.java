package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ReleaseDateValid;

import java.time.LocalDate;

/**
 * Film.
 */

@Data
public class Film {
    private Long id;

    @NotBlank(message = "Name не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private String description;

    @ReleaseDateValid // Собственная аннотация
    private LocalDate releaseDate;

    @Positive
    private Integer duration;
}