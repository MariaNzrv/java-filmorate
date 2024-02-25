package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    //создание пользователя
    User create(User user);

    //обновление пользователя
    User update(User user);

    //получение списка пользователей
    List<User> findAll();

    User findById(Integer userId);

    Boolean isUserExist(Integer userId);
}
