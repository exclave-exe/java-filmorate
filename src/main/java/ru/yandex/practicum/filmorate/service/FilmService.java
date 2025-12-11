package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.FilmCreateDto;
import ru.yandex.practicum.filmorate.dto.FilmResponseDto;
import ru.yandex.practicum.filmorate.dto.FilmUpdateDto;
import ru.yandex.practicum.filmorate.dto.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    public Collection<FilmResponseDto> getFilms() {
        return filmRepository.getFilms()
                .stream()
                .map(filmMapper::filmToResponseDto)
                .collect(Collectors.toList());
    }

    public FilmResponseDto getFilmById(long id) {
        return filmMapper.filmToResponseDto(validateFilmExists(id));
    }

    @Transactional
    public FilmResponseDto createFilm(FilmCreateDto filmCreateDto) {
        Film film = filmMapper.createDtoToFilm(filmCreateDto);
        validateFilmRefs(film);
        Long filmId = filmRepository.createFilm(film);
        Film createdFilm = validateFilmExists(filmId);
        log.info("Создан фильм id={}", createdFilm.getId());
        return filmMapper.filmToResponseDto(createdFilm);
    }

    @Transactional
    public FilmResponseDto updateFilm(FilmUpdateDto filmUpdateDto) {
        Film film = filmMapper.updateDtoToFilm(filmUpdateDto);
        validateFilmExists(film.getId());
        validateFilmRefs(film);
        filmRepository.updateFilm(film);
        Film updatedFilm = validateFilmExists(film.getId());
        log.info("Обновлен фильм id={}", updatedFilm.getId());
        return filmMapper.filmToResponseDto(updatedFilm);
    }

    public Collection<FilmResponseDto> getPopularFilms(long count) {
        return filmRepository.getPopularFilms(count)
                .stream()
                .map(filmMapper::filmToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addLike(long userId, long filmId) {
        validateFilmExists(filmId);
        validateUserExists(userId);
        filmRepository.addLike(filmId, userId);
    }

    @Transactional
    public void deleteLike(long userId, long filmId) {
        validateFilmExists(filmId);
        validateUserExists(userId);
        filmRepository.deleteLike(filmId, userId);
    }

    private void validateFilmRefs(Film film) {
        try {
            MpaType.fromId(film.getMpa().getId());
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("MPA с id=" + film.getMpa().getId() + " не найден");
        }

        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        for (Genre g : film.getGenres()) {
            try {
                GenreType.fromId(g.getId());
            } catch (IllegalArgumentException e) {
                throw new NotFoundException("Жанр с id=" + g.getId() + " не найден");
            }
        }
    }


    private User validateUserExists(long userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    private Film validateFilmExists(long filmId) {
        return filmRepository.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + filmId + " не найден"));
    }
}
