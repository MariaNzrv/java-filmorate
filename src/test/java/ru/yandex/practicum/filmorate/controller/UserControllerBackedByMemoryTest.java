package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.friendship.InMemoryFriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

class UserControllerBackedByMemoryTest extends AbstractUserControllerTest {

    @BeforeEach
    private void init() {
        userService = new UserService(new InMemoryUserStorage(), new InMemoryFriendshipStorage());
    }

}