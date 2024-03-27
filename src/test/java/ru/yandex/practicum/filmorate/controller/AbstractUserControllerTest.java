package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractUserControllerTest {
    protected UserService userService;

    @Test
    void userValidateOk() {
        User user = new User("mail@ya.ru", "pika");

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        user.setId(1);
        assertEquals(user1, user, "Пользователи не совпадают");
    }

    @Test
    void userEmptyLogin() {
        User user = new User("mail@ya.ru", "");

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userService.createUser(user));

        assertEquals("Логин не может содержать пробелы", ex.getMessage());
    }

    @Test
    void userEmptyEmail() {
        User user = new User("", "pika");

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userService.createUser(user));

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", ex.getMessage());
    }

    @Test
    void userEmailWithoutDog() {
        User user = new User("mail", "pika");

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userService.createUser(user));

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", ex.getMessage());
    }

    @Test
    void userBadBirthday() {
        User user = new User("mail@ya.ru", "pika");

        user.setBirthday(LocalDate.of(2995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userService.createUser(user));

        assertEquals("Дата рождения не может быть в будущем", ex.getMessage());
    }

    @Test
    void userEmptyName() {
        User user = new User("mail@ya.ru", "pika");

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));

        User user1 = userService.createUser(user);
        user.setId(1);
        assertEquals("pika", user1.getName(), "Имя не совпадает с ожидаемым");
        assertNotNull(user1.getName(), "Имя не заполнено");
    }

    @Test
    void updateUserWithoutId() {
        User user = new User("mail@ya.ru", "pika");

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
        User user1 = new User("mail@ya.ru", "pika");

        user1.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user1.setName("Пикачу");
        user1 = userService.createUser(user1);

        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        user2 = userService.createUser(user2);

        userService.addFriend(user1.getId(), user2.getId());

        assertEquals(userService.getFriends(user1.getId()).size(), 1, "Список друзей пуст");
        assertEquals(userService.getFriends(user2.getId()).size(), 0, "Список друзей должен быть пуст");
        assertEquals(userService.getFriends(user1.getId()).get(0).getId(), 2, "Неверный id друга");
    }

    @Test
    void addUnknownFriend() {
        User user = new User("mail@ya.ru", "pika");

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.addFriend(user1.getId(), 5));

        assertEquals("Невозможно добавить в друзья пользователя с Id = 5 . Его не существует в системе.", ex.getMessage());
    }

    @Test
    void addFriendToUnknownUser() {
        User user = new User("mail@ya.ru", "pika");

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.addFriend(5, user1.getId()));

        assertEquals("Пользователя с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void removeFriendOk() {
        User user = new User("mail@ya.ru", "pika");

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        userService.addFriend(user1.getId(), user2.getId());
        userService.removeFriend(user1.getId(), user2.getId());

        assertEquals(userService.getFriends(user1.getId()), new ArrayList<>(), "Список друзей не пуст");
        assertEquals(userService.getFriends(user3.getId()), new ArrayList<>(), "Список друзей не пуст");
    }

    @Test
    void removeUnknownFriend() {
        User user = new User("mail@ya.ru", "pika");

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
        User user1 = new User("mail@ya.ru", "pika");

        user1.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user1.setName("Пикачу");

        user1 = userService.createUser(user1);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        user2 = userService.createUser(user2);

        User user3 = new User("mail4@ya.ru", "pika4");
        user3.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 4));
        user3.setName("Пикачу4");
        user3 = userService.createUser(user3);

        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user3.getId(), user2.getId());
        List<User> commonFriends = userService.getCommonFriends(user1.getId(), user3.getId());

        assertNotNull(commonFriends, "Список друзей пуст");
        assertEquals(commonFriends.get(0), user2, "Список друзей отличается от ожидаемого");
        assertEquals(commonFriends.size(), 1, "Неверный размер списка друзей");
    }

    @Test
    void getCommonFriendFromUnknownUser() {
        User user1 = new User("mail@ya.ru", "pika");

        user1.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user1.setName("Пикачу");

        user1 = userService.createUser(user1);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        user2 = userService.createUser(user2);

        User user3 = new User("mail4@ya.ru", "pika4");
        user3.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 4));
        user3.setName("Пикачу4");
        user3 = userService.createUser(user3);

        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user3.getId(), user2.getId());

        User finalUser3 = user3;
        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getCommonFriends(5, finalUser3.getId()));

        assertEquals("Пользователя с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void getCommonFriendFromUnknownOtherUser() {
        User user1 = new User("mail@ya.ru", "pika");

        user1.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user1.setName("Пикачу");

        user1 = userService.createUser(user1);
        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        user2 = userService.createUser(user2);

        User user3 = new User("mail4@ya.ru", "pika4");
        user3.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 4));
        user3.setName("Пикачу4");
        user3 = userService.createUser(user3);

        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user3.getId(), user2.getId());

        User finalUser1 = user1;
        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.getCommonFriends(finalUser1.getId(), 5));

        assertEquals("Пользователя с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void findAllUsersOk() {
        User user1 = new User("mail@ya.ru", "pika");

        user1.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user1.setName("Пикачу");
        userService.createUser(user1);

        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        userService.createUser(user2);

        User user3 = new User("mail4@ya.ru", "pika4");
        user3.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 4));
        user3.setName("Пикачу4");
        userService.createUser(user3);

        List<User> usersList = userService.findAllUsers();

        assertNotNull(usersList, "Список друзей пуст");
        assertEquals(usersList.size(), 3, "Неверный размер списка друзей");
    }

    @Test
    void findUserByIdOk() {
        User user1 = new User("mail@ya.ru", "pika");

        user1.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user1.setName("Пикачу");
        user1 = userService.createUser(user1);

        User findUser = userService.findUserById(user1.getId());

        assertEquals(findUser, user1, "Пользователи не совпадают");
    }

    @Test
    void findUserByIdWithUnknownId() {

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userService.findUserById(10));

        assertEquals("Пользователя с Id = 10 не существует", ex.getMessage());
    }

}