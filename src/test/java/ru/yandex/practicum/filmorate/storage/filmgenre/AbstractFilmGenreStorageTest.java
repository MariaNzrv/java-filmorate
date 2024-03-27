package ru.yandex.practicum.filmorate.storage.filmgenre;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

abstract public class AbstractFilmGenreStorageTest {

    protected FilmStorage filmStorage;

    @Test
    public void testGetGenresByFilmId() {
        // Подготавливаем данные для теста
        Film film = new Film();
        film.setId(1);
        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));


        Genre newGenre = new Genre(1, "Комедия");

        List<Genre> genres = new ArrayList<>();
        genres.add(newGenre);

        film.setGenres(genres);
        filmStorage.create(film);

        // вызываем тестируемый метод
        List<Genre> savedGenres = filmStorage.getGenresByFilmId(1);

        // проверяем утверждения
        assertThat(savedGenres)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(genres);        // и сохраненного пользователя - совпадают
    }
}
