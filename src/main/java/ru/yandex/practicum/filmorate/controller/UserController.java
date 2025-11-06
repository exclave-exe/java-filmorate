package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Запрошен список пользователей");
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        log.info("Запрошено создание пользователя: {}", newUser.getEmail());
        return userService.createUser(newUser);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        log.info("Запрошено обновление пользователя userId={}", newUser.getId());
        return userService.updateUser(newUser);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable @Positive long id) {
        log.info("Запрошен список друзей userId={}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable @Positive long id,
                                             @PathVariable @Positive long otherId) {
        log.info("Запрошены общие друзья userId={} otherId={}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable @Positive long id,
                          @PathVariable @Positive long friendId) {
        log.info("Запрошено добавление в друзья userId={} friendId={}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable @Positive long id,
                             @PathVariable @Positive long friendId) {
        log.info("Запрошено удаление из друзей userId={} friendId={}", id, friendId);
        userService.deleteFriend(id, friendId);
    }
}