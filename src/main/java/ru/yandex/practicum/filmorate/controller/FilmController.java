package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Класс обслуживания фильмов.
 */
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final Integer MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private final HashMap<Integer, Film>  films = new HashMap<>();  // хранение фильмов в системе
    private Integer idCounter = 1; //счетчик id

    /**
     * получение списка всех фильмов.
     */
    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    /**
     * добавление фильма.
     */
    @PostMapping
    public Film create(@RequestBody Film film) {
        validate(film);

        Integer id = getUniqueId();

        film.setId(id);
        films.put(id, film);
        log.info("Создан фильм с Id: '{}'", id);
        return films.get(id);
    }


    /**
     * обновление фильма.
     */
    @PutMapping
    public Film update(@RequestBody Film film) {
        Integer id = film.getId();

        validate(film);
        if (id == null) {
            log.error("Id фильма не заполнен");
            throw new ValidationException("Для обновления данных фильма надо указать его Id");
        }
        if (!films.containsKey(id)) {
            log.error("Фильм с Id = {} не существует", id);
            throw new ValidationException("Фильм с таким Id не существует");
        }


        films.put(id, film);
        log.info("Обновлен фильм с Id: '{}'", id);
        return films.get(id);
    }

    private Integer getUniqueId() {
        // вычисление уникального Id
        Integer result = idCounter;
        idCounter++;
        return result;
    }

    private void validate(Film film) {
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Long duration = film.getDuration();
        String name = film.getName();

        if (name.isBlank()) {
            log.warn("название не может быть пустым");
            throw new ValidationException("название не может быть пустым");
        }
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Размер описания больше чем {} символов", MAX_DESCRIPTION_LENGTH);
            throw new ValidationException("максимальная длина описания — " + MAX_DESCRIPTION_LENGTH + " символов");
        }
        if (releaseDate != null && releaseDate.isBefore(MIN_RELEASE_DATE)) {
            log.warn("Дата релиза раньше, чем {}", MIN_RELEASE_DATE);
            throw new ValidationException("дата релиза — не раньше " + MIN_RELEASE_DATE);
        }
        if (duration != null && duration <= 0) {
            log.warn("Продолжительность фильма {} минут, а должна быть положительной", film.getDuration());
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }


    }
}
