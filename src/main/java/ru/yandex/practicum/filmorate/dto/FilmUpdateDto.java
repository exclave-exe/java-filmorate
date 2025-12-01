package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class FilmUpdateDto {
    @NotNull
    private Long id;

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
