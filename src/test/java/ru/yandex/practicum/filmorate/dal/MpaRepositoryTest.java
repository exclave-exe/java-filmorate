package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaRepository.class, MpaRowMapper.class})
class MpaRepositoryTest {
    private final MpaRepository mpaRepository;

    @Test
    void shouldReturnAllMpaWithCorrectSizeAndOrder() {
        List<Mpa> all = mpaRepository.getAllMpa();

        assertThat(all)
                .hasSize(5)
                .extracting(Mpa::getName)
                .containsExactly("G", "PG", "PG-13", "R", "NC-17");
    }

    @Test
    void shouldFindExistingMpaById() {
        Optional<Mpa> mpaOpt = mpaRepository.getMpaById(1);

        assertThat(mpaOpt)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                    assertThat(mpa.getId()).isEqualTo(1);
                    assertThat(mpa.getName()).isEqualTo("G");
                });
    }

    @Test
    void shouldReturnEmptyWhenMpaNotFound() {
        Optional<Mpa> mpaOpt = mpaRepository.getMpaById(999);

        assertThat(mpaOpt).isEmpty();
    }
}
