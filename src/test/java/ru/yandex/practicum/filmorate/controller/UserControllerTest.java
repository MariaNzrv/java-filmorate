package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    @Test
    void userValidateOk() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        user.setId(1);
        assertEquals(user1, user, "Пользователи не совпадают");
    }

    @Test
    void userEmptyLogin() {
        User user = new User("mail@ya.ru", "");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userService.createUser(user));

        assertEquals("Логин не может содержать пробелы", ex.getMessage());
    }

    @Test
    void userEmptyEmail() {
        User user = new User("", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userService.createUser(user));

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", ex.getMessage());
    }

    @Test
    void userEmailWithoutDog() {
        User user = new User("mail", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userService.createUser(user));

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", ex.getMessage());
    }

    @Test
    void userBadBirthday() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(2995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userService.createUser(user));

        assertEquals("Дата рождения не может быть в будущем", ex.getMessage());
    }

    @Test
    void userEmptyName() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));

        User user1 = userService.createUser(user);
        user.setId(1);
        assertEquals("pika", user1.getName(), "Имя не совпадает с ожидаемым");
        assertNotNull(user1.getName(), "Имя не заполнено");
    }

    @Test
    void updateUserWithoutId() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        ValidationException ex = assertThrows(ValidationException.class, () -> userService.updateUser(user2));

        assertEquals("Для обновления данных пользователя надо указать его Id", ex.getMessage());
    }

    @Test
    void updateUserWithUnknownId() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        user2.setId(34);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.updateUser(user2));

        assertEquals("Пользователя с таким Id не существует", ex.getMessage());
    }

    @Test
    void updateUserOk() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        user2.setId(1);
        User user3 = userService.updateUser(user2);

        assertEquals(user2, user3, "Пользователи не совпадают");
    }

    @Test
    void addFriendOk() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        userService.addFriend(user1.getId(), user2.getId());

        assertNotNull(user1.getFriends(), "Список друзей пуст");
        assertNotNull(user3.getFriends(), "Список друзей пуст");
        assertEquals(user1.getFriends().stream().iterator().next(), 2, "Неверный id друга");
        assertEquals(user3.getFriends().stream().iterator().next(), 1, "Неверный id друга");
    }

    @Test
    void addUnknownFriend() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.addFriend(user1.getId(), 5));

        assertEquals("Невозможно добавить в друзья пользователя с Id = 5 . Его не существует в системе.", ex.getMessage());
    }

    @Test
    void addFriendToUnknownUser() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.addFriend(5, user1.getId()));

        assertEquals("Пользователя с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void removeFriendOk() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        userService.addFriend(user1.getId(), user2.getId());
        userService.removeFriend(user1.getId(), user2.getId());

        assertEquals(user1.getFriends(), new HashSet<>(),  "Список друзей не пуст");
        assertEquals(user3.getFriends(), new HashSet<>(), "Список друзей не пуст");
    }

    @Test
    void removeUnknownFriend() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        userService.addFriend(user1.getId(), user3.getId());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.removeFriend(user1.getId(), 5));

        assertEquals("Пользователя с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void removeFriendFromUnknownUser() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        userService.addFriend(user1.getId(), user3.getId());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.removeFriend(5, user3.getId()));

        assertEquals("Пользователя с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void getFriendOk() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        userService.addFriend(user1.getId(), user3.getId());

        assertNotNull(userService.getFriends(user1.getId()), "Список друзей пуст");
        assertEquals(userService.getFriends(user1.getId()).iterator().next(), user3,
                "Список друзей отличается от ожидаемого");
        assertEquals(userService.getFriends(user1.getId()).size(), 1, "Неверный размер списка друзей");
    }

    @Test
    void getFriendsFromUnknownUser() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        userService.addFriend(user1.getId(), user3.getId());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getFriends(5));

        assertEquals("Пользователя с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void getCommonFriendOk() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        User user4 = new User("mail4@ya.ru", "pika4");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 4));
        user2.setName("Пикачу4");
        User user5 = userService.createUser(user4);

        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user5.getId(), user3.getId());
        List<User> commonFriends = userService.getCommonFriends(user1.getId(), user5.getId());

        assertNotNull(commonFriends, "Список друзей пуст");
        assertEquals(commonFriends.get(0), user3, "Список друзей отличается от ожидаемого");
        assertEquals(commonFriends.size(), 1, "Неверный размер списка друзей");
    }

    @Test
    void getCommonFriendFromUnknownUser() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        User user4 = new User("mail4@ya.ru", "pika4");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 4));
        user2.setName("Пикачу4");
        User user5 = userService.createUser(user4);

        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user5.getId(), user3.getId());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getCommonFriends(5, user5.getId()));

        assertEquals("Пользователя с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void getCommonFriendFromUnknownOtherUser() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        User user4 = new User("mail4@ya.ru", "pika4");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 4));
        user2.setName("Пикачу4");
        User user5 = userService.createUser(user4);

        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user5.getId(), user3.getId());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getCommonFriends(user1.getId(), 5));

        assertEquals("Пользователя с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void findAllUsersOk() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        User user4 = new User("mail4@ya.ru", "pika4");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 4));
        user2.setName("Пикачу4");
        User user5 = userService.createUser(user4);

        List<User> usersList = userService.findAllUsers();

        assertNotNull(usersList, "Список друзей пуст");
        assertEquals(usersList.size(), 3, "Неверный размер списка друзей");
    }

    @Test
    void findUserByIdOk() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        User user4 = new User("mail4@ya.ru", "pika4");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 4));
        user2.setName("Пикачу4");
        User user5 = userService.createUser(user4);

        User findUser = userService.findUserById(user1.getId());

        assertEquals(findUser, user1, "Пользователи не совпадают");
    }

    @Test
    void findUserByIdWithUnknownId() {
        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(new InMemoryUserStorage());

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        User user4 = new User("mail4@ya.ru", "pika4");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 4));
        user2.setName("Пикачу4");
        User user5 = userService.createUser(user4);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.findUserById(10));

        assertEquals("Пользователя с Id = 10 не существует", ex.getMessage());
    }

}