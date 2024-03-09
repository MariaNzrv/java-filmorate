package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

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
}
