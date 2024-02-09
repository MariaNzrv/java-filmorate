package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    @Test
    void userValidateOk() {
        User user = new User("mail@ya.ru","pika");
        UserController userController = new UserController();

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userController.create(user);
        user.setId(1);
        assertEquals(user1, user, "Пользователи не совпадают");
    }

    @Test
    void userEmptyLogin(){
        User user = new User("mail@ya.ru","");
        UserController userController = new UserController();

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(user));

        assertEquals("Логин не может содержать пробелы", ex.getMessage());
    }

    @Test
    void userEmptyEmail(){
        User user = new User("","pika");
        UserController userController = new UserController();

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(user));

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", ex.getMessage());
    }

    @Test
    void userEmailWithoutDog(){
        User user = new User("mail","pika");
        UserController userController = new UserController();

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(user));

        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", ex.getMessage());
    }

    @Test
    void userBadBirthday(){
        User user = new User("mail@ya.ru","pika");
        UserController userController = new UserController();

        user.setBirthday(LocalDate.of(2995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");


        ValidationException ex = assertThrows(ValidationException.class, () -> userController.create(user));

        assertEquals("Дата рождения не может быть в будущем", ex.getMessage());
    }

    @Test
    void userEmptyName() {
        User user = new User("mail@ya.ru","pika");
        UserController userController = new UserController();

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));

        User user1 = userController.create(user);
        user.setId(1);
        assertEquals("pika", user1.getName(), "Имя не совпадает с ожидаемым");
        assertNotNull(user1.getName(), "Имя не заполнено");
    }

    @Test
    void createUserWithExistId() {
        User user = new User("mail@ya.ru","pika");
        UserController userController = new UserController();

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userController.create(user);
        User user2 = new User("mail2@ya.ru","pika2");
        user2.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user2.setName("Пикачу2");
        user2.setId(1);
        UserAlreadyExistException ex = assertThrows(UserAlreadyExistException.class, () -> userController.create(user2));

        assertEquals("Пользователь уже существует", ex.getMessage());
    }

    @Test
    void updateUserWithoutId() {
        User user = new User("mail@ya.ru","pika");
        UserController userController = new UserController();

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userController.create(user);
        User user2 = new User("mail2@ya.ru","pika");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.update(user2));

        assertEquals("Для обновления данных пользователя надо указать его Id", ex.getMessage());
    }

    @Test
    void updateUserWithUnknownId() {
        User user = new User("mail@ya.ru","pika");
        UserController userController = new UserController();

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userController.create(user);
        User user2 = new User("mail2@ya.ru","pika");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        user2.setId(34);
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.update(user2));

        assertEquals("Пользователя с таким Id не существует", ex.getMessage());
    }

    @Test
    void updateUserOk() {
        User user = new User("mail@ya.ru","pika");
        UserController userController = new UserController();

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userController.create(user);
        User user2 = new User("mail2@ya.ru","pika");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        user2.setId(1);
        User user3 = userController.update(user2);

        assertEquals(user2, user3, "Пользователи не совпадают");
    }

}