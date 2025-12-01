package ru.yandex.practicum.filmorate.dto.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FilmMapper {

    public FilmResponseDto toResponseDto(Film film) {
        if (film == null) {
            return null;
        }

        FilmResponseDto dto = new FilmResponseDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());
        dto.setMpa(film.getMpa());
        dto.setGenres(film.getGenres());
        return dto;
    }

    public Film fromCreateDto(FilmCreateDto dto) {
        Film film = new Film();
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());

        Mpa mpa = new Mpa();
        mpa.setId(dto.getMpaId());
        film.setMpa(mpa);

        if (dto.getGenres() != null && !dto.getGenres().isEmpty()) {
            Set<Genre> genres = dto.getGenres().stream()
                    .map(id -> {
                        Genre g = new Genre();
                        g.setId(id);
                        return g;
                    })
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            film.setGenres(genres);
        }

        return film;
    }

    public Film fromUpdateDto(FilmUpdateDto dto) {
        Film film = fromCreateDto(new FilmCreateDto(
                dto.getName(),
                dto.getDescription(),
                dto.getReleaseDate(),
                dto.getDuration(),
                dto.getMpaId(),
                dto.getGenres()
        ));
        film.setId(dto.getId());
        return film;
    }
}
