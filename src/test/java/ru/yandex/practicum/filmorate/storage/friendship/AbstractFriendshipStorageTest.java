package ru.yandex.practicum.filmorate.storage.friendship;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.FriendStatusCode;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

abstract class AbstractFriendshipStorageTest {
    protected FriendshipStorage friendshipStorage;
    protected UserStorage userStorage;

    @Test
    public void testAddFriendship() {
        // Подготавливаем данные для теста

        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2@email.ru", "ira123", "Ira Borisova", LocalDate.of(1997, 7, 15));
        userStorage.create(newUser);
        userStorage.create(newUser2);

        friendshipStorage.addFriendship(newUser.getId(), newUser2.getId(), FriendStatusCode.UNCONFIRMED.name());

        List<Integer> friends = friendshipStorage.getFriends(1);

        List<Integer> result = new ArrayList<>();
        result.add(2);

        // проверяем утверждения
        assertThat(friends)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(result);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testRemoveFriendship() {
        // Подготавливаем данные для теста

        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2@email.ru", "ira123", "Ira Borisova", LocalDate.of(1997, 7, 15));
        User newUser3 = new User(3, "user3@email.ru", "eva123", "Eva Polna", LocalDate.of(1985, 3, 8));
        userStorage.create(newUser);
        userStorage.create(newUser2);
        userStorage.create(newUser3);

        friendshipStorage.addFriendship(newUser.getId(), newUser2.getId(), FriendStatusCode.UNCONFIRMED.name());
        friendshipStorage.addFriendship(newUser.getId(), newUser3.getId(), FriendStatusCode.UNCONFIRMED.name());

        friendshipStorage.removeFriendship(newUser.getId(), newUser2.getId());

        List<Integer> friends = friendshipStorage.getFriends(1);

        List<Integer> result = new ArrayList<>();
        result.add(3);

        // проверяем утверждения
        assertThat(friends)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(result);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testGetFriends() {
        // Подготавливаем данные для теста
        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2@email.ru", "ira123", "Ira Borisova", LocalDate.of(1997, 7, 15));
        User newUser3 = new User(3, "user3@email.ru", "eva123", "Eva Polna", LocalDate.of(1985, 3, 8));
        userStorage.create(newUser);
        userStorage.create(newUser2);
        userStorage.create(newUser3);

        friendshipStorage.addFriendship(newUser.getId(), newUser2.getId(), FriendStatusCode.UNCONFIRMED.name());
        friendshipStorage.addFriendship(newUser.getId(), newUser3.getId(), FriendStatusCode.UNCONFIRMED.name());

        List<Integer> friends = friendshipStorage.getFriends(1);

        List<Integer> result = new ArrayList<>();
        result.add(2);
        result.add(3);

        // проверяем утверждения
        assertThat(friends)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(result);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testUpdateFriendship() {
        // Подготавливаем данные для теста
        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2@email.ru", "ira123", "Ira Borisova", LocalDate.of(1997, 7, 15));
        userStorage.create(newUser);
        userStorage.create(newUser2);

        friendshipStorage.addFriendship(newUser.getId(), newUser2.getId(), FriendStatusCode.UNCONFIRMED.name());
        friendshipStorage.updateFriendship(newUser.getId(), newUser2.getId(), FriendStatusCode.CONFIRMED.name());

        List<Integer> friends = friendshipStorage.getFriends(2);

        List<Integer> result = new ArrayList<>();
        result.add(1);

        // проверяем утверждения
        assertThat(friends)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(result);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testGetFriendship() {
        // Подготавливаем данные для теста
        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2@email.ru", "ira123", "Ira Borisova", LocalDate.of(1997, 7, 15));
        userStorage.create(newUser);
        userStorage.create(newUser2);

        friendshipStorage.addFriendship(newUser.getId(), newUser2.getId(), FriendStatusCode.UNCONFIRMED.name());

        Friendship friendship = new Friendship(newUser.getId(), newUser2.getId());
        Friendship savedFriendship = friendshipStorage.getFriendship(newUser2.getId(), newUser.getId());

        // проверяем утверждения
        assertThat(savedFriendship)
                .isNotNull() // проверяем, что объект не равен null
                .isEqualTo(friendship);
    }
}
