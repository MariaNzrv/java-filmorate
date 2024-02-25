package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private final HashMap<Integer, User> users = new HashMap<>();  // хранение пользователей в системе
    private Integer idCounter = 1; //счетчик id

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        Integer id = getUniqueId();

        user.setId(id);
        users.put(id, user);
        log.info("Создан пользователь с Id: '{}'", id);
        return users.get(id);
    }

    @Override
    public User update(User user) {
        Integer id = user.getId();

        users.put(id, user);
        log.info("Обновлен пользователь с Id: '{}'", id);
        return users.get(id);
    }

    @Override
    public User findById(Integer userId){
        return users.get(userId);
    }

    @Override
    public Boolean isUserExist(Integer userId){
        return users.containsKey(userId);
    }

    private Integer getUniqueId() {
        // вычисление уникального Id
        Integer result = idCounter;
        idCounter++;
        return result;
    }


}
