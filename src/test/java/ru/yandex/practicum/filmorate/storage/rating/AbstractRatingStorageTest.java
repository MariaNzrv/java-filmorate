package ru.yandex.practicum.filmorate.storage.rating;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

abstract class AbstractRatingStorageTest {
    protected RatingStorage ratingStorage;

    @Test
    public void testFindRatingById() {
        // Подготавливаем данные для теста
        Rating newRating = new Rating(1, "G", "Нет возрастных ограничений");

        // вызываем тестируемый метод
        Rating savedRating = ratingStorage.findById(1);

        // проверяем утверждения
        assertThat(savedRating)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newRating);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindRatingWithBadId() {
        // Подготавливаем данные для теста
        Rating newRating = new Rating(1, "G", "Нет возрастных ограничений");

        // вызываем тестируемый метод
        Rating savedRating = ratingStorage.findById(17);

        // проверяем утверждения
        assertThat(savedRating)
                .isNull();
    }

    @Test
    public void testFindAllRatings() {
        // Подготавливаем данные для теста
        Rating rating1 = new Rating(1, "G", "Нет возрастных ограничений");
        Rating rating2 = new Rating(2, "PG", "Рекомендуется присутствие родителей");
        Rating rating3 = new Rating(3, "PG-13", "Детям до 13 лет просмотр не желателен");
        Rating rating4 = new Rating(4, "R", "Лицам до 17 лет обязательно присутствие взрослого");
        Rating rating5 = new Rating(5, "NC-17", "Лицам до 18 лет просмотр запрещен");

        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating1);
        ratings.add(rating2);
        ratings.add(rating3);
        ratings.add(rating4);
        ratings.add(rating5);

        // вызываем тестируемый метод
        List<Rating> savedRatings = ratingStorage.findAll();

        // проверяем утверждения
        assertThat(savedRatings)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(ratings);        // и сохраненного пользователя - совпадают
    }
}
