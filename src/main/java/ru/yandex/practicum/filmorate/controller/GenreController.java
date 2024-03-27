package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

/**
 * Класс обслуживания жанров.
 */
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    @Autowired
    private final GenreService genreService;

    /**
     * получение списка всех жанров.
     */
    @GetMapping
    public List<Genre> findAll() {
        return genreService.findAllGenres();
    }

    /**
     * получение жанра по Id
     */
    @GetMapping("/{id}")
    public Genre findById(@PathVariable Integer id) {
        return genreService.findGenreById(id);
    }
}
