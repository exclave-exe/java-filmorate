package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FriendshipRepository.class, UserRepository.class, UserRowMapper.class})
class FriendshipRepositoryTest {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    private long createUser(String email, String login, String name, LocalDate birthday) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        Long id = userRepository.createUser(user);
        user.setId(id);
        return id;
    }

    @Test
    void shouldAddAndGetFriends() {
        long userId = createUser("u1@mail.ru", "user1", "User 1", LocalDate.of(2000, 1, 1));
        long friendId1 = createUser("u2@mail.ru", "user2", "User 2", LocalDate.of(2000, 2, 2));
        long friendId2 = createUser("u3@mail.ru", "user3", "User 3", LocalDate.of(2000, 3, 3));

        friendshipRepository.addFriend(userId, friendId1);
        friendshipRepository.addFriend(userId, friendId2);

        List<User> friends = friendshipRepository.getFriends(userId);

        assertThat(friends)
                .hasSize(2)
                .extracting(User::getId)
                .containsExactlyInAnyOrder(friendId1, friendId2);
    }

    @Test
    void shouldDeleteFriend() {
        long userId = createUser("u1@mail.ru", "user1", "User 1", LocalDate.of(2000, 1, 1));
        long friendId = createUser("u2@mail.ru", "user2", "User 2", LocalDate.of(2000, 2, 2));
        friendshipRepository.addFriend(userId, friendId);
        assertThat(friendshipRepository.getFriends(userId)).hasSize(1);

        friendshipRepository.deleteFriend(userId, friendId);

        assertThat(friendshipRepository.getFriends(userId)).isEmpty();
    }

    @Test
    void shouldReturnCommonFriends() {
        long user1 = createUser("u1@mail.ru", "user1", "User 1", LocalDate.of(2000, 1, 1));
        long user2 = createUser("u2@mail.ru", "user2", "User 2", LocalDate.of(2000, 2, 2));
        long commonFriend = createUser("u3@mail.ru", "user3", "User 3", LocalDate.of(2000, 3, 3));
        long otherFriend = createUser("u4@mail.ru", "user4", "User 4", LocalDate.of(2000, 4, 4));

        friendshipRepository.addFriend(user1, commonFriend);
        friendshipRepository.addFriend(user2, commonFriend);
        friendshipRepository.addFriend(user1, otherFriend);

        List<User> common = friendshipRepository.getCommonFriends(user1, user2);

        assertThat(common)
                .hasSize(1)
                .first()
                .extracting(User::getId)
                .isEqualTo(commonFriend);
    }
}
