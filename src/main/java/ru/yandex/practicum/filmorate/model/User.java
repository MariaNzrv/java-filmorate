package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Пользователь.
 */

@Data
public class User {
    private Integer id; // целочисленный идентификатор
    @NonNull
    private String email; // электронная почта
    @NonNull
    private String login; // логин пользователя
    private String name; // имя для отображения
    private LocalDate birthday; // дата рождения
    private Set<Integer> friends = new HashSet<>(); // список друзей (состоит из Id пользователей, которые считаются друзьями)
}
