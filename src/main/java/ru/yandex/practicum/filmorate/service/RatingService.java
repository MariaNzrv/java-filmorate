package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {
    @Autowired
    private final RatingStorage ratingStorage;

    public List<Rating> findAllRatings() {
        return ratingStorage.findAll();
    }

    public Rating findRatingById(Integer ratingId) {
        Rating rating = ratingStorage.findById(ratingId);
        if (rating == null) {
            log.error("Рейтинг с Id = {} не существует", ratingId);
            throw new RuntimeException("Рейтинг с Id = " + ratingId + " не существует");
        }
        return rating;
    }
}
