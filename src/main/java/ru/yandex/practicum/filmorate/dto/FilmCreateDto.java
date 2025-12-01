package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class FilmCreateDto {
    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @NotNull
    @PastOrPresent
    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    @NotNull
    private Integer mpaId;

    private Set<Integer> genres;
}

