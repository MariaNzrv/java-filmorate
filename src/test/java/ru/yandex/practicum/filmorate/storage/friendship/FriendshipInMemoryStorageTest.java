package ru.yandex.practicum.filmorate.storage.friendship;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

public class FriendshipInMemoryStorageTest extends AbstractFriendshipStorageTest {
    @BeforeEach
    private void init() {
        friendshipStorage = new InMemoryFriendshipStorage();
        userStorage = new InMemoryUserStorage();
    }
}
