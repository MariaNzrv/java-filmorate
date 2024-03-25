package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    // получение всех жанров
    List<Genre> findAll();

    // получение жанра по id
    Genre findById(Integer genreId);

    // получение списка жанров фильма
    List<Genre> getGenresByFilmId(Integer filmId);

    Boolean isGenreExist(Integer genreId);

}
