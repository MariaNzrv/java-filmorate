package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    // добавление друга
    void addFriendship(Integer userId, Integer friendId, String status);

    // удаление друга
    void removeFriendship(Integer userId, Integer friendId);

    // получение списка друзей пользователя
    List<Integer> getFriends(Integer userId);

    // обновление дружбы
    void updateFriendship(Integer userId, Integer friendId, String status);

    // поиск дружбы
    Friendship getFriendship(Integer userId, Integer friendId);
}
