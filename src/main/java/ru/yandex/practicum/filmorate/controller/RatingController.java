package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

/**
 * Класс обслуживания возрастных рейтингов.
 */
@RestController
@RequestMapping("/mpa")
public class RatingController {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * получение списка всех возрастных рейтингов.
     */
    @GetMapping
    public List<Rating> findAll() {
        return ratingService.findAllRatings();
    }

    /**
     * получение возрастного рейтинга по Id
     */
    @GetMapping("/{id}")
    public Rating findById(@PathVariable Integer id) {
        return ratingService.findRatingById(id);
    }
}
