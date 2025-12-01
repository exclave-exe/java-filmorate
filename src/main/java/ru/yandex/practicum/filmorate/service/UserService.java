package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.UserUpdateDto;
import ru.yandex.practicum.filmorate.dto.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Collection<UserResponseDto> getUsers() {
        return userRepository.getUsers()
                .stream()
                .map(userMapper::userToResponseDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(long id) {
        return userMapper.userToResponseDto(validateUserExists(id));
    }

    @Transactional
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        User user = userMapper.createDtoToUser(userCreateDto);
        Long userId = userRepository.createUser(user);
        User createdUser = validateUserExists(userId);
        log.info("Создан пользователь: id={}, email={}", createdUser.getId(), createdUser.getEmail());
        return userMapper.userToResponseDto(createdUser);
    }

    @Transactional
    public UserResponseDto updateUser(UserUpdateDto userUpdateDto) {
        User user = userMapper.updateDtoToUser(userUpdateDto);
        validateUserExists(user.getId());
        userRepository.updateUser(user);
        User updatedUser = validateUserExists(user.getId());
        log.info("Обновлен пользователь: id={}", updatedUser.getId());
        return userMapper.userToResponseDto(updatedUser);
    }

    private User validateUserExists(long userId) {
        return userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException(
                "Пользователь с id=" + userId + " не найден"));
    }
}
