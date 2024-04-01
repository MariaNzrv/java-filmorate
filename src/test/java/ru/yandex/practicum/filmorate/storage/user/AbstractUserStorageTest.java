package ru.yandex.practicum.filmorate.storage.user;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

abstract class AbstractUserStorageTest {
    protected UserStorage userStorage;

    @Test
    public void testFindUserById() {
        // Подготавливаем данные для теста
        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        userStorage.create(newUser);

        // вызываем тестируемый метод
        User savedUser = userStorage.findById(1);

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindUserWithBadId() {
        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        userStorage.create(newUser);

        // вызываем тестируемый метод
        User savedUser = userStorage.findById(2);

        // проверяем утверждения
        assertThat(savedUser)
                .isNull(); // проверяем, что объект null
    }

    @Test
    public void testUserExist() {
        // Подготавливаем данные для теста
        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        userStorage.create(newUser);

        // вызываем тестируемый метод
        Boolean result = userStorage.isUserExist(1);

        // проверяем утверждения
        assertThat(result)
                .isNotNull() // проверяем, что объект не равен null
                .isEqualTo(true);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testUserNotExist() {
        // Подготавливаем данные для теста
        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        userStorage.create(newUser);

        // вызываем тестируемый метод
        Boolean result = userStorage.isUserExist(2);

        // проверяем утверждения
        assertThat(result)
                .isNotNull() // проверяем, что объект не равен null
                .isEqualTo(false);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindAllUsers() {
        // Подготавливаем данные для теста
        User newUser1 = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2@email.ru", "ira123", "Ira Borisova", LocalDate.of(1997, 7, 15));
        userStorage.create(newUser1);
        userStorage.create(newUser2);
        List<User> users = new ArrayList<>();
        users.add(newUser1);
        users.add(newUser2);

        // вызываем тестируемый метод
        List<User> savedUsers = userStorage.findAll();

        // проверяем утверждения
        assertThat(savedUsers)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(users);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testUpdateUser() {
        // Подготавливаем данные для теста
        User newUser1 = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(1, "user2@email.ru", "ira123", "Ira Borisova", LocalDate.of(1997, 7, 15));
        userStorage.create(newUser1);

        // вызываем тестируемый метод
        User savedUser = userStorage.update(newUser2);

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser2);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testCreateUser() {
        // Подготавливаем данные для теста
        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));

        // вызываем тестируемый метод
        User savedUser = userStorage.create(newUser);

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }
}
