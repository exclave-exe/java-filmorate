package ru.yandex.practicum.filmorate.dto.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.LinkedHashSet;

@Component
public class FilmMapper {

    public FilmResponseDto filmToResponseDto(Film film) {
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

    public Film createDtoToFilm(FilmCreateDto filmCreateDto) {
        Film film = new Film();
        film.setName(filmCreateDto.getName());
        film.setDescription(filmCreateDto.getDescription());
        film.setReleaseDate(filmCreateDto.getReleaseDate());
        film.setDuration(filmCreateDto.getDuration());
        film.setMpa(filmCreateDto.getMpa());
        film.setGenres(
                filmCreateDto.getGenres() == null
                        ? null
                        : new LinkedHashSet<>(filmCreateDto.getGenres())
        );
        return film;
    }

    public Film updateDtoToFilm(FilmUpdateDto filmUpdateDto) {
        Film film = new Film();
        film.setId(filmUpdateDto.getId());
        film.setName(filmUpdateDto.getName());
        film.setDescription(filmUpdateDto.getDescription());
        film.setReleaseDate(filmUpdateDto.getReleaseDate());
        film.setDuration(filmUpdateDto.getDuration());
        film.setMpa(filmUpdateDto.getMpa());
        film.setGenres(
                filmUpdateDto.getGenres() == null
                        ? null
                        : new LinkedHashSet<>(filmUpdateDto.getGenres())
        );
        return film;
    }
}

