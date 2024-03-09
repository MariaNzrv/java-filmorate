package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.util.Objects;

/**
 * Дружба.
 */
@Data
public class Friendship {
    @NonNull
    private Integer userId; // Id пользователя
    @NonNull
    private Integer friendId; // Id друга
    private FriendStatusCode statusCode; // статус дружбы

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(userId, that.userId) && Objects.equals(friendId, that.friendId) ||
                Objects.equals(friendId, that.userId) && Objects.equals(userId, that.friendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId);
    }
}
