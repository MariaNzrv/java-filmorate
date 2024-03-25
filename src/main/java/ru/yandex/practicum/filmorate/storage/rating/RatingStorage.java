package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingStorage {
    // получение всех рейтингов
    List<Rating> findAll();

    // получение рейтинга по id
    Rating findById(Integer ratingId);

    Boolean isRatingExist(Integer ratingId);
}
