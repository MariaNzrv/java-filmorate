package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class Rating {
    // рейтинг
    @NonNull
    private RatingCode name; // название рейтинга
    private String description; // описание рейтинга
}
