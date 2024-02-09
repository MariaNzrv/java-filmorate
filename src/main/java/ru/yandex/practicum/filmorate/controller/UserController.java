package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Класс обслуживания пользователей.
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();  // хранение пользователей в системе
    private Integer idCounter = 1; //счетчик id

    /**
     * получение списка всех пользователей.
     */
    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    /**
     * создание пользователя.
     */
    @PostMapping
    public User create(@RequestBody User user) {
        Integer idUser = user.getId();
        Integer id = idUser;

        validate(user);

        if (idUser != null && users.containsKey(idUser)){
            log.error("Пользователь с id = {} уже есть в системе", idUser);
            throw new UserAlreadyExistException("Пользователь уже существует");
        }

        if (idUser == null) {
            id = getUniqueId();
        } else {
            idCounter = id + 1;
        }
        user.setId(id);
        users.put(id, user);
        log.info("Создан пользователь с Id: '{}'", id);
        return users.get(id);
    }


    /**
     * обновление пользователя.
     */
    @PutMapping
    public User update(@RequestBody User user) {
        Integer id = user.getId();

        validate(user);
        if (id == null){
            log.error("Id не заполнен");
            throw new ValidationException("Для обновления данных пользователя надо указать его Id");
        }
        if (!users.containsKey(id)){
            log.error("Пользователя с Id = {} не существует", id);
            throw new ValidationException("Пользователя с таким Id не существует");
        }


        users.put(id, user);
        log.info("Обновлен пользователь с Id: '{}'", id);
        return users.get(id);
    }

    private Integer getUniqueId(){
        // вычисление уникального Id
        Integer result = idCounter;
        idCounter++;
        return result;
    }

    private static void validate(User user) {
        String email = user.getEmail();
        String login = user.getLogin();
        if (email.isBlank() || email.indexOf('@') == -1){
            log.error("Эл. почта: {}", email);
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (login.isBlank()){
            log.error("Логин: {}", login);
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())){
            log.error("Дата рождения: {}", user.getBirthday());
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isBlank()){
            user.setName(login);
        }
    }

}
