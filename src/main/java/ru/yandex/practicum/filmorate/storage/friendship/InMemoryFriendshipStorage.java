package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendStatusCode;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryFriendshipStorage implements FriendshipStorage {
    private final List<Friendship> friendshipSet = new ArrayList<>();

    // получения друзей по Id
    @Override
    public List<Integer> getFriends(Integer userId) {
        List<Integer> friends = new ArrayList<>();
        for (Friendship friendship : friendshipSet) {
            if (friendship.getUserId().equals(userId)) {
                friends.add(friendship.getFriendId());
            } else if (friendship.getFriendId().equals(userId) && friendship.getStatusCode() == FriendStatusCode.CONFIRMED) {
                friends.add(friendship.getUserId());
            }
        }
        return friends;
    }

    // добавление друга
    @Override
    public void addFriendship(Integer userId, Integer friendId, String status) {
        Friendship friendship = getFriendship(userId, friendId);
        if (friendship == null) {
            friendship = new Friendship(userId, friendId);
            friendship.setStatusCode(FriendStatusCode.valueOf(status));
            friendshipSet.add(friendship);
        } else {
            friendship.setStatusCode(FriendStatusCode.valueOf(status));
        }
    }

    // удаление друга
    @Override
    public void removeFriendship(Integer userId, Integer friendId) {
        friendshipSet.removeIf(f ->
                (f.getUserId().equals(userId) && f.getFriendId().equals(friendId))
                        || (f.getFriendId().equals(userId) && f.getUserId().equals(friendId))
        );
    }

    // обновление дружбы
    @Override
    public void updateFriendship(Integer userId, Integer friendId, String status) {
        Friendship friendship = getFriendship(userId, friendId);
        if (friendship != null) {
            friendship.setStatusCode(FriendStatusCode.valueOf(status));
        }
    }

    // поиск дружбы
    @Override
    public Friendship getFriendship(Integer userId, Integer friendId) {
        return friendshipSet.stream()
                .filter(f -> (f.getUserId().equals(userId) && f.getFriendId().equals(friendId))
                        || (f.getFriendId().equals(userId) && f.getUserId().equals(friendId)))
                .findFirst()
                .orElse(null);
    }
}
