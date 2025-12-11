package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.dto.UserUpdateDto;

public class UsernameUpdateDtoValidator implements ConstraintValidator<UsernameValid, UserUpdateDto> {

    @Override
    public boolean isValid(UserUpdateDto user, ConstraintValidatorContext context) {
        if (user == null) {
            return true;
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        return true;
    }
}
