package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User getUser(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            log.error("Пользователь не найден userId={}", userId);
            throw new NotFoundException("Пользователь с ID: " + userId + " не найден.");
        }
        return user;
    }

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public User createUser(User newUser) {
        long newId = getNextId();
        newUser.setId(newId);
        users.put(newId, newUser);
        log.info("Пользователь создан userId={}", newId);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        if (!users.containsKey(newUser.getId())) {
            log.error("Обновление невозможно — пользователь не найден userId={}", newUser.getId());
            throw new NotFoundException("Пользователь с ID: " + newUser.getId() + " не найден.");
        }

        User oldUser = users.get(newUser.getId());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setName(newUser.getName());
        oldUser.setBirthday(newUser.getBirthday());
        log.debug("Пользователь обновлён userId={}", newUser.getId());
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
