package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({
        FilmRepository.class,
        UserRepository.class,
        MpaRepository.class,
        FilmRowMapper.class,
        GenreRowMapper.class,
        MpaRowMapper.class,
        UserRowMapper.class
})
class FilmRepositoryTest {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    private Film buildFilm(String name, String desc, LocalDate date, int duration, int mpaId, Set<Genre> genres) {
        Film f = new Film();
        f.setName(name);
        f.setDescription(desc);
        f.setReleaseDate(date);
        f.setDuration(duration);
        f.setMpa(new Mpa(mpaId, null));
        f.setGenres(genres);
        return f;
    }

    private User buildUser(String email, String login) {
        User u = new User();
        u.setEmail(email);
        u.setLogin(login);
        u.setName(login);
        u.setBirthday(LocalDate.of(2000, 1, 1));
        return u;
    }

    @Test
    void shouldCreateAndFindFilmByIdWithGenresAndMpa() {
        Film toSave = buildFilm(
                "Film 1",
                "Desc",
                LocalDate.of(2000, 1, 1),
                100,
                1,
                Set.of(new Genre(1, null), new Genre(2, null))
        );

        Long id = filmRepository.createFilm(toSave);
        toSave.setId(id);

        Optional<Film> fromDb = filmRepository.getFilmById(id);

        assertThat(fromDb)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getId()).isEqualTo(id);
                    assertThat(film.getName()).isEqualTo("Film 1");
                    assertThat(film.getMpa().getId()).isEqualTo(1);
                    assertThat(film.getGenres())
                            .extracting(Genre::getId)
                            .containsExactly(1, 2);
                });
    }

    @Test
    void shouldUpdateFilmAndGenres() {
        Film toSave = buildFilm(
                "Old",
                "Old desc",
                LocalDate.of(2000, 1, 1),
                90,
                1,
                Set.of(new Genre(1, null))
        );
        Long id = filmRepository.createFilm(toSave);
        toSave.setId(id);

        toSave.setName("New");
        toSave.setDescription("New desc");
        toSave.setDuration(150);
        toSave.setGenres(Set.of(new Genre(2, null), new Genre(3, null)));

        filmRepository.updateFilm(toSave);

        Optional<Film> fromDb = filmRepository.getFilmById(id);

        assertThat(fromDb)
                .isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film.getName()).isEqualTo("New");
                    assertThat(film.getDescription()).isEqualTo("New desc");
                    assertThat(film.getDuration()).isEqualTo(150);
                    assertThat(film.getGenres())
                            .extracting(Genre::getId)
                            .containsExactly(2, 3);
                });
    }

    @Test
    void shouldReturnPopularFilmsByLikes() {
        Film film1 = buildFilm("Film 1", "d1",
                LocalDate.of(2000, 1, 1), 90, 1, Set.of());
        Film film2 = buildFilm("Film 2", "d2",
                LocalDate.of(2001, 1, 1), 100, 1, Set.of());

        Long id1 = filmRepository.createFilm(film1);
        Long id2 = filmRepository.createFilm(film2);

        User u1 = buildUser("u1@mail.ru", "u1");
        User u2 = buildUser("u2@mail.ru", "u2");
        Long u1Id = userRepository.createUser(u1);
        Long u2Id = userRepository.createUser(u2);

        filmRepository.addLike(id1, u1Id);
        filmRepository.addLike(id1, u2Id);
        filmRepository.addLike(id2, u1Id);

        List<Film> popular = (List<Film>) filmRepository.getPopularFilms(10);

        assertThat(popular)
                .hasSize(2)
                .extracting(Film::getId)
                .containsExactly(id1, id2);
    }

    @Test
    void shouldDeleteLike() {
        Film film = buildFilm("Film", "d",
                LocalDate.of(2000, 1, 1), 90, 1, Set.of());
        Long filmId = filmRepository.createFilm(film);

        User u = buildUser("u@mail.ru", "u");
        Long userId = userRepository.createUser(u);

        filmRepository.addLike(filmId, userId);
        assertThat(filmRepository.getPopularFilms(10))
                .extracting(Film::getId)
                .contains(filmId);

        filmRepository.deleteLike(filmId, userId);
        assertThat(filmRepository.getFilmById(filmId)).isPresent();
    }
}
