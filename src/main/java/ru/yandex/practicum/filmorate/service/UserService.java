package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers().values();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> getFriends(long userId) {
        User user = userStorage.getUser(userId);
        Set<Long> friendsId = user.getFriendsId();

        if (friendsId.isEmpty()) {
            log.debug("Список друзей пуст userId={}", userId);
            return List.of();
        }

        Collection<User> friends = friendsId.stream()
                .map(userStorage.getUsers()::get)
                .collect(Collectors.toList());
        log.debug("Найдено друзей: {} userId={}", friends.size(), userId);
        return friends;
    }

    public Collection<User> getCommonFriends(long userId, long otherUserId) {
        User user = userStorage.getUser(userId);
        User otherUser = userStorage.getUser(otherUserId);

        if (user.getFriendsId().isEmpty() || otherUser.getFriendsId().isEmpty()) {
            log.debug("Общие друзья отсутствуют userId={} otherId={}", userId, otherUserId);
            return List.of();
        }

        Set<Long> commonIds = user.getFriendsId().stream()
                .filter(otherUser.getFriendsId()::contains)
                .collect(Collectors.toSet());

        Collection<User> commonFriends = commonIds.stream()
                .map(userStorage.getUsers()::get)
                .collect(Collectors.toList());

        log.debug("Найдено общих друзей: {} userId={} otherId={}", commonFriends.size(), userId, otherUserId);
        return commonFriends;
    }

    public void addFriend(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        boolean userAdded = user.getFriendsId().add(friend.getId());
        boolean friendAdded = friend.getFriendsId().add(user.getId());

        if (userAdded && friendAdded) {
            log.info("Пользователи стали друзьями userId={} friendId={}", userId, friendId);
        } else {
            log.debug("Дружба уже существовала userId={} friendId={}", userId, friendId);
        }
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        boolean userRemoved = user.getFriendsId().remove(friend.getId());
        boolean friendRemoved = friend.getFriendsId().remove(user.getId());

        if (userRemoved && friendRemoved) {
            log.info("Пользователи перестали быть друзьями userId={} friendId={}", userId, friendId);
        } else {
            log.debug("Дружба отсутствовала userId={} friendId={}", userId, friendId);
        }
    }
}
