package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.dto.UserCreateDto;

public class UsernameCreateDtoValidator implements ConstraintValidator<UsernameValid, UserCreateDto> {

    @Override
    public boolean isValid(UserCreateDto user, ConstraintValidatorContext context) {
        if (user == null) {
            return true;
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return true;
    }
}
