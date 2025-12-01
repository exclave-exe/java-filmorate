package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> {
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_USER = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> rowMapper) {
        super(jdbc, rowMapper);
    }

    public List<User> getUsers() {
        return findMany(SELECT_ALL_USERS);
    }

    public Optional<User> getUserById(Long userId) {
        return findOne(SELECT_USER_BY_ID, userId);
    }

    public Long createUser(User newUser) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newUser.getEmail());
            ps.setString(2, newUser.getLogin());
            ps.setString(3, newUser.getName());
            ps.setDate(4, Date.valueOf(newUser.getBirthday()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void updateUser(User updatedUser) {
        jdbcTemplate.update(UPDATE_USER,
                updatedUser.getEmail(),
                updatedUser.getLogin(),
                updatedUser.getName(),
                Date.valueOf(updatedUser.getBirthday()),
                updatedUser.getId());
    }
}
