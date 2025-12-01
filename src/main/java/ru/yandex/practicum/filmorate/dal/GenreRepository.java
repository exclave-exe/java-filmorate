package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {
    private static final String SELECT_ALL_GENRES = "SELECT * FROM genres ORDER BY id";
    private static final String SELECT_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getAllGenres() {
        return findMany(SELECT_ALL_GENRES);
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        return findOne(SELECT_GENRE_BY_ID, genreId);
    }
}

