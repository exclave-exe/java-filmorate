package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

public class UserResponseDto extends BaseRepository<User> {
    public UserResponseDto(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

}
