package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    public User getUser(Long userId);

    public Map<Long, User> getUsers();

    public User createUser(User newUser);

    public User updateUser(User newUser);
}
