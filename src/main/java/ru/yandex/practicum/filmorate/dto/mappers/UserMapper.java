package ru.yandex.practicum.filmorate.dto.mappers;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class UserResponseMapper {

    public static UserResponseDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setLogin(user.getLogin());
        dto.setName(user.getName());
        dto.setBirthday(user.getBirthday());
        return dto;
    }
}

