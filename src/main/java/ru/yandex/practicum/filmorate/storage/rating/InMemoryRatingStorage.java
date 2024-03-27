package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryRatingStorage implements RatingStorage {
    private final Map<Integer, Rating> ratings = Stream.of(
            new Rating(1, "G", "Нет возрастных ограничений"),
            new Rating(2, "PG", "Рекомендуется присутствие родителей"),
            new Rating(3, "PG-13", "Детям до 13 лет просмотр не желателен"),
            new Rating(4, "R", "Лицам до 17 лет обязательно присутствие взрослого"),
            new Rating(5, "NC-17", "Лицам до 18 лет просмотр запрещен")
    ).collect(Collectors.toMap(Rating::getId, Function.identity()));

    @Override
    public List<Rating> findAll() {
        return new ArrayList<>(ratings.values());
    }

    @Override
    public Rating findById(Integer ratingId) {
        return ratings.get(ratingId);
    }
}
