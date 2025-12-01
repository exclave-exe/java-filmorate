package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<Mpa> {
    private static final String SELECT_ALL_MPA = "SELECT * FROM mpa ORDER BY id";
    private static final String SELECT_MPA_BY_ID = "SELECT * FROM mpa WHERE id = ?";

    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> getAllMpa() {
        return findMany(SELECT_ALL_MPA);
    }

    public Optional<Mpa> getMpaById(Integer mpaId) {
        return findOne(SELECT_MPA_BY_ID, mpaId);
    }
}
