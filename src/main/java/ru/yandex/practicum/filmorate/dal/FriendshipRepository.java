package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
public class FriendshipRepository extends BaseRepository<User> {
    private static final String INSERT_FRIENDSHIP = "INSERT INTO friendships (requester_id, addressee_id) VALUES (?, ?)";
    private static final String DELETE_FRIENDSHIP = "DELETE FROM friendships WHERE requester_id = ? AND addressee_id = ?";
    private static final String SELECT_USER_FRIENDS = """
            SELECT u.*
            FROM users u
            JOIN friendships f ON u.id = f.addressee_id
            WHERE f.requester_id = ?
            """;

    private static final String SELECT_COMMON_FRIENDS = """
            SELECT u.*
            FROM users u
            WHERE u.id IN (
                SELECT f1.addressee_id
                FROM friendships f1
                WHERE f1.requester_id = ?
                INTERSECT
                SELECT f2.addressee_id
                FROM friendships f2
                WHERE f2.requester_id = ?
            )
            """;

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<User> rowMapper) {
        super(jdbc, rowMapper);
    }

    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update(INSERT_FRIENDSHIP, userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        jdbcTemplate.update(DELETE_FRIENDSHIP, userId, friendId);
    }

    public List<User> getFriends(long userId) {
        return findMany(SELECT_USER_FRIENDS, userId);
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        return findMany(SELECT_COMMON_FRIENDS, userId, otherId);
    }
}
