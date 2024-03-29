package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FriendshipService {
    private final Set<Friendship> friendshipSet = new HashSet<>();

    // получения друзей по Id
    public List<Integer> getFriends(Integer userId) {
        List<Integer> friends = new ArrayList<>();
        for (Friendship friendship : friendshipSet) {
            if (friendship.getUserId() == userId) {
                friends.add(friendship.getFriendId());
            } else if (friendship.getFriendId() == userId) {
                friends.add(friendship.getUserId());
            }
        }
        return friends;
    }

    // добавление друга
    public void addFriendship(Integer userId, Integer friendId) {
        Friendship friendship = new Friendship(userId, friendId);
        friendshipSet.add(friendship);
    }

    // удаление друга
    public void removeFriendship(Integer userId, Integer friendId) {
        Friendship friendship = new Friendship(userId, friendId);
        if (friendshipSet.contains(friendship)) {
            friendshipSet.remove(friendship);
        }
    }
}
