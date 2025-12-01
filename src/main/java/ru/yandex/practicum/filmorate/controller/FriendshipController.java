package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserResponseDto;
import ru.yandex.practicum.filmorate.service.FriendshipService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FriendshipController {
    private final FriendshipService friendshipService;

    @GetMapping("/{id}/friends")
    public Collection<UserResponseDto> getFriends(@PathVariable @Positive long id) {
        log.info("Запрошен список друзей userId={}", id);
        return friendshipService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<UserResponseDto> getCommonFriends(@PathVariable @Positive long id,
                                                        @PathVariable @Positive long otherId) {
        log.info("Запрошены общие друзья userId={} otherId={}", id, otherId);
        return friendshipService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable @Positive long id,
                          @PathVariable @Positive long friendId) {
        log.info("Запрошено добавление в друзья userId={} friendId={}", id, friendId);
        friendshipService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable @Positive long id,
                             @PathVariable @Positive long friendId) {
        log.info("Запрошено удаление из друзей userId={} friendId={}", id, friendId);
        friendshipService.deleteFriend(id, friendId);
    }
}