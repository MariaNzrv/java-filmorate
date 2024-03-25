package ru.yandex.practicum.filmorate.storage.friendship;

import java.util.List;

public interface FriendshipStorage {
    // добавление друга
    void addFriendship(Integer userId, Integer friendId);

    // удаление друга
    void removeFriendship(Integer userId, Integer friendId);

    // получение списка друзей пользователя
    List<Integer> getFriends(Integer userId);
}
