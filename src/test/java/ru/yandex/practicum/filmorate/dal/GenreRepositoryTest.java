package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreRepository.class, GenreRowMapper.class})
class GenreRepositoryTest {
    private final GenreRepository genreRepository;

    @Test
    void shouldReturnAllGenresWithCorrectSizeAndOrder() {
        List<Genre> all = genreRepository.getAllGenres();

        assertThat(all)
                .hasSize(6)
                .extracting(Genre::getName)
                .containsExactly(
                        "Комедия",
                        "Драма",
                        "Мультфильм",
                        "Триллер",
                        "Документальный",
                        "Боевик"
                );
    }

    @Test
    void shouldFindGenreById() {
        Optional<Genre> genreOpt = genreRepository.getGenreById(1);

        assertThat(genreOpt)
                .isPresent()
                .hasValueSatisfying(genre -> {
                    assertThat(genre.getId()).isEqualTo(1);
                    assertThat(genre.getName()).isEqualTo("Комедия");
                });
    }

    @Test
    void shouldReturnEmptyWhenGenreNotFound() {
        Optional<Genre> genreOpt = genreRepository.getGenreById(999);

        assertThat(genreOpt).isEmpty();
    }
}
