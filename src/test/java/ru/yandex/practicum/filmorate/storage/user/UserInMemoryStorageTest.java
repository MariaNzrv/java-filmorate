package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.BeforeEach;

public class UserInMemoryStorageTest extends AbstractUserStorageTest {
    @BeforeEach
    private void init() {
        userStorage = new InMemoryUserStorage();
    }
}
