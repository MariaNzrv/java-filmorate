package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, @Qualifier("friendshipDbStorage") FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    private static void validate(User user) {
        String email = user.getEmail();
        String login = user.getLogin();
        if (email.isBlank() || email.indexOf('@') == -1) {
            log.warn("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (login.isBlank()) {
            log.warn("Логин не может содержать пробелы");
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(login);
        }
    }

    // добавление в друзья
    public void addFriend(Integer userId, Integer friendId) {
        if (userStorage.isUserExist(userId)) {
            if (userStorage.isUserExist(friendId)) {
                friendshipStorage.addFriendship(userId, friendId);
            } else {
                log.error("Невозможно добавить в друзья пользователя с Id = {}, его не существует", friendId);
                throw new RuntimeException("Невозможно добавить в друзья пользователя с Id = " + friendId +
                        " . Его не существует в системе.");
            }
        } else {
            log.error("Пользователя с Id = {} не существует", userId);
            throw new RuntimeException("Пользователя с Id = " + userId + " не существует");
        }
    }

    // удаление из друзей
    public void removeFriend(Integer userId, Integer friendId) {
        if (userStorage.isUserExist(userId)) {
            if (userStorage.isUserExist(friendId)) {
                friendshipStorage.removeFriendship(userId, friendId);
            } else {
                log.error("Пользователя с Id = {} не существует", friendId);
                throw new RuntimeException("Пользователя с Id = " + friendId + " не существует");
            }
        } else {
            log.error("Пользователя с Id = {} не существует", userId);
            throw new RuntimeException("Пользователя с Id = " + userId + " не существует");
        }
    }

    // получение списка друзей
    public List<User> getFriends(Integer userId) {
        if (!userStorage.isUserExist(userId)) {
            log.error("Пользователя с Id = {} не существует", userId);
            throw new RuntimeException("Пользователя с Id = " + userId + " не существует");
        }
        ArrayList<User> userFriends = new ArrayList<>();
        for (Integer friendId : friendshipStorage.getFriends(userId)) {
            userFriends.add(userStorage.findById(friendId));
        }
        return userFriends;
    }

    // получение списка друзей, общих с другим пользователем
    public List<User> getCommonFriends(Integer userId, Integer otherUserId) {
        if (!userStorage.isUserExist(userId)) {
            log.error("Пользователя с Id = {} не существует", userId);
            throw new RuntimeException("Пользователя с Id = " + userId + " не существует");
        }
        if (!userStorage.isUserExist(otherUserId)) {
            log.error("Пользователя с Id = {} не существует", otherUserId);
            throw new RuntimeException("Пользователя с Id = " + otherUserId + " не существует");
        }
        List<Integer> userFriendsIds = friendshipStorage.getFriends(userId);
        List<Integer> otherUserFriendsIds = friendshipStorage.getFriends(otherUserId);
        ArrayList<User> userFriends = new ArrayList<>();
        if (userFriendsIds != null && otherUserFriendsIds != null) {
            for (Integer id : userFriendsIds) {
                if (otherUserFriendsIds.contains(id)) {
                    userFriends.add(userStorage.findById(id));
                }
            }
        }
        return userFriends;
    }

    public List<User> findAllUsers() {
        return userStorage.findAll();
    }

    public User findUserById(Integer userId) {
        if (!userStorage.isUserExist(userId)) {
            log.error("Пользователя с Id = {} не существует", userId);
            throw new RuntimeException("Пользователя с Id = " + userId + " не существует");
        }
        return userStorage.findById(userId);
    }

    public User createUser(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        Integer id = user.getId();

        validate(user);
        if (id == null) {
            log.error("Id не заполнен");
            throw new ValidationException("Для обновления данных пользователя надо указать его Id");
        }
        if (!userStorage.isUserExist(id)) {
            log.error("Пользователя с Id = {} не существует", id);
            throw new RuntimeException("Пользователя с таким Id не существует");
        }
        return userStorage.update(user);
    }
}
