package ru.yandex.practicum.filmorate.storage.genre;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

abstract class AbstractGenreStorageTest {
    protected GenreStorage genreStorage;

    @Test
    public void testFindGenreById() {
        // Подготавливаем данные для теста
        Genre newGenre = new Genre(1, "Комедия");

        // вызываем тестируемый метод
        Genre savedGenre = genreStorage.findById(1);

        // проверяем утверждения
        assertThat(savedGenre)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newGenre);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindGenreWithBadId() {
        // Подготавливаем данные для теста

        // вызываем тестируемый метод
        Genre savedGenre = genreStorage.findById(17);

        // проверяем утверждения
        assertThat(savedGenre)
                .isNull();
    }


    @Test
    public void testFindAllGenres() {
        // Подготавливаем данные для теста
        Genre newGenre1 = new Genre(1, "Комедия");
        Genre newGenre2 = new Genre(2, "Драма");
        Genre newGenre3 = new Genre(3, "Мультфильм");
        Genre newGenre4 = new Genre(4, "Триллер");
        Genre newGenre5 = new Genre(5, "Документальный");
        Genre newGenre6 = new Genre(6, "Боевик");


        List<Genre> genres = new ArrayList<>();
        genres.add(newGenre1);
        genres.add(newGenre2);
        genres.add(newGenre3);
        genres.add(newGenre4);
        genres.add(newGenre5);
        genres.add(newGenre6);

        // вызываем тестируемый метод
        List<Genre> savedGenres = genreStorage.findAll();

        // проверяем утверждения
        assertThat(savedGenres)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(genres);        // и сохраненного пользователя - совпадают
    }
}
