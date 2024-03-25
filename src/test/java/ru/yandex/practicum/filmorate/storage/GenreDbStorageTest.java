package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenreName;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class GenreDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindGenreById() {
        // Подготавливаем данные для теста
        Genre newGenre = new Genre(1, GenreName.Комедия);
        GenreDbStorage genreStorage = new GenreDbStorage(jdbcTemplate);

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
        GenreDbStorage genreStorage = new GenreDbStorage(jdbcTemplate);

        // вызываем тестируемый метод
        Genre savedGenre = genreStorage.findById(17);

        // проверяем утверждения
        assertThat(savedGenre)
                .isNull();
    }

    @Test
    public void testGenreExist() {
        // Подготавливаем данные для теста
        GenreDbStorage genreStorage = new GenreDbStorage(jdbcTemplate);

        // вызываем тестируемый метод
        Boolean result = genreStorage.isGenreExist(1);

        // проверяем утверждения
        assertThat(result)
                .isNotNull() // проверяем, что объект не равен null
                .isEqualTo(true);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testGenreNotExist() {
        // Подготавливаем данные для теста
        GenreDbStorage genreStorage = new GenreDbStorage(jdbcTemplate);

        // вызываем тестируемый метод
        Boolean result = genreStorage.isGenreExist(17);

        // проверяем утверждения
        assertThat(result)
                .isNotNull() // проверяем, что объект не равен null
                .isEqualTo(false);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindAllGenres() {
        // Подготавливаем данные для теста
        Genre newGenre1 = new Genre(1, GenreName.Комедия);
        Genre newGenre2 = new Genre(2, GenreName.Драма);
        Genre newGenre3 = new Genre(3, GenreName.Мультфильм);
        Genre newGenre4 = new Genre(4, GenreName.Триллер);
        Genre newGenre5 = new Genre(5, GenreName.Документальный);
        Genre newGenre6 = new Genre(6, GenreName.Боевик);

        GenreDbStorage genreStorage = new GenreDbStorage(jdbcTemplate);

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

    @Test
    public void testGetGenresByFilmId() {
        // Подготавливаем данные для теста
        Film film = new Film();
        film.setId(1);
        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);

        Genre newGenre = new Genre(1, GenreName.Комедия);
        GenreDbStorage genreStorage = new GenreDbStorage(jdbcTemplate);

        List<Genre> genres = new ArrayList<>();
        genres.add(newGenre);

        film.setGenres(genres);
        filmStorage.create(film);

        // вызываем тестируемый метод
        List<Genre> savedGenres = genreStorage.getGenresByFilmId(1);

        // проверяем утверждения
        assertThat(savedGenres)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(genres);        // и сохраненного пользователя - совпадают
    }
}
