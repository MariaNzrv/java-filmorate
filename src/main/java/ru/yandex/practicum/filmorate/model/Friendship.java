package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

/**
 * Дружба.
 */
@Data
public class Friendship {
    @NonNull
    private Integer userId; // Id пользователя
    @NonNull
    private Integer friendId; // Id друга
    private FriendStatusCode statusCode = FriendStatusCode.UNCONFIRMED; // статус дружбы
}
