package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validation.ReleaseDateValid;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class FilmCreateDto {
    @NotBlank(message = "Name не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не должно превышать 200 символов")
    private String description;

    @ReleaseDateValid // Собственная аннотация
    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    @NotNull
    private Mpa mpa;

    private Set<Genre> genres;
}

