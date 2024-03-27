package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmStorage {

    //создание фильма
    Film create(Film film);

    //обновление фильма
    Film update(Film film);

    //получение всех фильмов
    List<Film> findAll();

    Film findById(Integer filmId);

    Boolean isFilmExist(Integer filmId);

    // добавление лайка
    void saveLike(Integer userId, Integer filmId);

    // удаление лайка
    void removeLike(Integer userId, Integer filmId);

    // получение списка жанров фильма
    List<Genre> getGenresByFilmId(Integer filmId);

    // получение списка жанров фильма
    Set<Integer> getLikesByFilmId(Integer filmId);
}
