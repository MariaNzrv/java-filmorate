package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class RatingController {
    @Autowired
    private final RatingService ratingService;

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
