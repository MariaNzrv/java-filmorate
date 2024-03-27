package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

/**
 * Класс обслуживания фильмов.
 */
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    @Autowired
    private final FilmService filmService;

    /**
     * получение списка всех фильмов.
     */
    @GetMapping
    public List<Film> findAll() {
        return filmService.findAllFilms();
    }

    /**
     * получение фильма по Id
     */
    @GetMapping("/{id}")
    public Film findById(@PathVariable Integer id) {
        return filmService.findFilmById(id);
    }

    /**
     * получение списка популярных фильмов
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        return filmService.getMostLikeFilms(count);
    }

    /**
     * добавление фильма.
     */
    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.createFilm(film);
    }


    /**
     * обновление фильма.
     */
    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    /**
     * проставление лайка.
     */
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    /**
     * удаление лайка.
     */
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
    }

}
