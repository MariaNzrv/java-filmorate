package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.List;

@Slf4j
@Service
public class RatingService {
    private final RatingStorage ratingStorage;

    @Autowired
    public RatingService(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    public List<Rating> findAllRatings() {
        return ratingStorage.findAll();
    }

    public Rating findRatingById(Integer ratingId) {
        if (!ratingStorage.isRatingExist(ratingId)) {
            log.error("Рейтинг с Id = {} не существует", ratingId);
            throw new RuntimeException("Рейтинг с Id = " + ratingId + " не существует");
        }
        return ratingStorage.findById(ratingId);
    }
}
