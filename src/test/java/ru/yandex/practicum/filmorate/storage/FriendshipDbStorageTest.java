package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class FriendshipDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testAddFriendship() {
        // Подготавливаем данные для теста
        FriendshipDbStorage friendshipStorage = new FriendshipDbStorage(jdbcTemplate);

        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2@email.ru", "ira123", "Ira Borisova", LocalDate.of(1997, 7, 15));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        userStorage.create(newUser2);

        friendshipStorage.addFriendship(newUser.getId(), newUser2.getId());

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
        FriendshipDbStorage friendshipStorage = new FriendshipDbStorage(jdbcTemplate);

        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2@email.ru", "ira123", "Ira Borisova", LocalDate.of(1997, 7, 15));
        User newUser3 = new User(3, "user3@email.ru", "eva123", "Eva Polna", LocalDate.of(1985, 3, 8));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        userStorage.create(newUser2);
        userStorage.create(newUser3);

        friendshipStorage.addFriendship(newUser.getId(), newUser2.getId());
        friendshipStorage.addFriendship(newUser.getId(), newUser3.getId());

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
        FriendshipDbStorage friendshipStorage = new FriendshipDbStorage(jdbcTemplate);

        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2@email.ru", "ira123", "Ira Borisova", LocalDate.of(1997, 7, 15));
        User newUser3 = new User(3, "user3@email.ru", "eva123", "Eva Polna", LocalDate.of(1985, 3, 8));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        userStorage.create(newUser2);
        userStorage.create(newUser3);

        friendshipStorage.addFriendship(newUser.getId(), newUser2.getId());
        friendshipStorage.addFriendship(newUser.getId(), newUser3.getId());

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
}
