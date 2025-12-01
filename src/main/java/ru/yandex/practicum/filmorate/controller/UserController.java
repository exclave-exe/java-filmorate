package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.UserUpdateDto;
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
    public Collection<UserResponseDto> getUsers() {
        log.info("Запрошен список пользователей");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable @Positive long id) {
        log.info("Запрошен пользователь id={}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserCreateDto newUser) {
        log.info("Запрошено создание пользователя: {}", newUser.getEmail());
        return userService.createUser(newUser);
    }

    @PutMapping
    public UserResponseDto updateUser(@Valid @RequestBody UserUpdateDto updatedUser) {
        log.info("Запрошено обновление пользователя userId={}", updatedUser.getId());
        return userService.updateUser(updatedUser);
    }
}