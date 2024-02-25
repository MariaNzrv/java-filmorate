package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    //создание фильма
    Film create (Film film);

    //обновление фильма
    Film update (Film film);

    //получение всех фильмов
    List<Film> findAll();

    Film findById(Integer filmId);

    Boolean isFilmExist(Integer filmId);
}
