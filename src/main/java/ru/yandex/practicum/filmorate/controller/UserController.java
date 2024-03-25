package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

/**
 * Класс обслуживания пользователей.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * получение списка всех пользователей.
     */
    @GetMapping
    public List<User> findAll() {
        return userService.findAllUsers();
    }

    /**
     * получение пользователя по Id
     */
    @GetMapping("/{id}")
    public User findById(@PathVariable Integer id) {
        return userService.findUserById(id);
    }

    /**
     * получение списка друзей пользователя
     */
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        return userService.getFriends(id);
    }

    /**
     * получение списка друзей, общих с другим пользователем
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    /**
     * создание пользователя.
     */
    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * обновление пользователя.
     */
    @PutMapping
    public User update(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * добавление в друзья.
     */
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriend(id, friendId);
    }

    /**
     * удаление из друзей.
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.removeFriend(id, friendId);
    }

}
