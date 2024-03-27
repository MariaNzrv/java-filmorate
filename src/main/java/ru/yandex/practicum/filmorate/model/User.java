package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

/**
 * Пользователь.
 */

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id; // целочисленный идентификатор
    @NonNull
    private String email; // электронная почта
    @NonNull
    private String login; // логин пользователя
    private String name; // имя для отображения
    private LocalDate birthday; // дата рождения
}
