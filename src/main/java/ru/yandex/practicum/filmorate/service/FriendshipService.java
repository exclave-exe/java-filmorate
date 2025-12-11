package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.dto.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Collection<UserResponseDto> getFriends(long userId) {
        validateUserExists(userId);
        return friendshipRepository.getFriends(userId)
                .stream()
                .map(userMapper::userToResponseDto)
                .collect(Collectors.toList());
    }

    public Collection<UserResponseDto> getCommonFriends(long userId, long otherId) {
        validateUserExists(userId);
        validateUserExists(otherId);
        return friendshipRepository.getCommonFriends(userId, otherId)
                .stream()
                .map(userMapper::userToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addFriend(long userId, long friendId) {
        if (userId == friendId) {
            throw new ValidationException("Нельзя добавить самого себя в друзья");
        }
        validateUserExists(userId);
        validateUserExists(friendId);
        friendshipRepository.addFriend(userId, friendId);
        log.info("Пользователь id={} добавил в друзья пользователя id={}", userId, friendId);
    }

    @Transactional
    public void deleteFriend(long userId, long friendId) {
        validateUserExists(userId);
        validateUserExists(friendId);
        friendshipRepository.deleteFriend(userId, friendId);
        log.info("Пользователь id={} удалил из друзей пользователя id={}", userId, friendId);
    }

    private User validateUserExists(long userId) {
        return userRepository.getUserById(userId).orElseThrow(() -> new NotFoundException(
                "Пользователь с id=" + userId + " не найден"));
    }
}
