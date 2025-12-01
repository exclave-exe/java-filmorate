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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, UserRowMapper.class})
class UserRepositoryTest {
    private final UserRepository userRepository;

    private User buildUser(String email, String login, String name, LocalDate birthday) {
        User u = new User();
        u.setEmail(email);
        u.setLogin(login);
        u.setName(name);
        u.setBirthday(birthday);
        return u;
    }

    @Test
    void shouldCreateAndFindUserById() {
        User toSave = buildUser(
                "user1@mail.ru",
                "user1",
                "User One",
                LocalDate.of(2000, 1, 1)
        );
        Long id = userRepository.createUser(toSave);
        toSave.setId(id);

        Optional<User> fromDb = userRepository.getUserById(id);

        assertThat(fromDb)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getId()).isEqualTo(id);
                    assertThat(user.getEmail()).isEqualTo("user1@mail.ru");
                    assertThat(user.getLogin()).isEqualTo("user1");
                    assertThat(user.getName()).isEqualTo("User One");
                    assertThat(user.getBirthday()).isEqualTo(LocalDate.of(2000, 1, 1));
                });
    }

    @Test
    void shouldReturnAllUsers() {
        User u1 = buildUser("u1@mail.ru", "u1", "User 1", LocalDate.of(1990, 1, 1));
        User u2 = buildUser("u2@mail.ru", "u2", "User 2", LocalDate.of(1995, 2, 2));

        Long id1 = userRepository.createUser(u1);
        Long id2 = userRepository.createUser(u2);
        u1.setId(id1);
        u2.setId(id2);

        List<User> users = userRepository.getUsers();

        assertThat(users)
                .hasSize(2)
                .extracting(User::getLogin)
                .containsExactlyInAnyOrder("u1", "u2");
    }

    @Test
    void shouldUpdateUser() {
        User u = buildUser("old@mail.ru", "oldLogin", "Old Name", LocalDate.of(1990, 1, 1));
        Long id = userRepository.createUser(u);
        u.setId(id);
        u.setEmail("new@mail.ru");
        u.setLogin("newLogin");
        u.setName("New Name");
        u.setBirthday(LocalDate.of(2001, 2, 2));
        userRepository.updateUser(u);

        Optional<User> fromDb = userRepository.getUserById(id);

        assertThat(fromDb)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getEmail()).isEqualTo("new@mail.ru");
                    assertThat(user.getLogin()).isEqualTo("newLogin");
                    assertThat(user.getName()).isEqualTo("New Name");
                    assertThat(user.getBirthday()).isEqualTo(LocalDate.of(2001, 2, 2));
                });
    }
}
