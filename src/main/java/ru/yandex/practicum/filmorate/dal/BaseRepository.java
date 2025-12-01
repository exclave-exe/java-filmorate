package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseRepository <T> {
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> rowMapper;

    protected Optional<T> findOne(String sqlQuery, Object... args){
        try {
            T result = jdbcTemplate.queryForObject(sqlQuery, rowMapper, args);
            return Optional.of(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    protected List<T> findMany(String sqlQuery, Object... args){
        return jdbcTemplate.query(sqlQuery, args, rowMapper);
    }
}

