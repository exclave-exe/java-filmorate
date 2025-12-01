package ru.yandex.practicum.filmorate.dto.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.UserUpdateDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserMapper {
    public UserResponseDto userToResponseDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setLogin(user.getLogin());
        userResponseDto.setName(user.getName());
        userResponseDto.setBirthday(user.getBirthday());
        return userResponseDto;
    }

    public User createDtoToUser(UserCreateDto userCreateDto) {
        if (userCreateDto == null) {
            return null;
        }

        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setLogin(userCreateDto.getLogin());
        user.setName(userCreateDto.getName());
        user.setBirthday(userCreateDto.getBirthday());
        return user;
    }

    public User updateDtoToUser(UserUpdateDto userUpdateDto) {
        if (userUpdateDto == null) {
            return null;
        }

        User user = new User();
        user.setId(userUpdateDto.getId());
        user.setEmail(userUpdateDto.getEmail());
        user.setLogin(userUpdateDto.getLogin());
        user.setName(userUpdateDto.getName());
        user.setBirthday(userUpdateDto.getBirthday());
        return user;
    }
}