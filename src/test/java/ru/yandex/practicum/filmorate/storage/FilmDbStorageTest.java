package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindFilmById() {
        // Подготавливаем данные для теста
        Film film = new Film();
        film.setId(1);
        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(film);

        // вызываем тестируемый метод
        Film savedFilm = filmStorage.findById(1);

        // проверяем утверждения
        assertThat(savedFilm)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(film);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindFilmWithBadId() {
        // Подготавливаем данные для теста
        Film film = new Film();
        film.setId(1);
        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(film);

        // вызываем тестируемый метод
        Film savedFilm = filmStorage.findById(2);

        // проверяем утверждения
        assertThat(savedFilm)
                .isNull(); // проверяем, что объект не равен null
    }

    @Test
    public void testFilmExist() {
        // Подготавливаем данные для теста
        Film film = new Film();
        film.setId(1);
        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(film);

        // вызываем тестируемый метод
        Boolean result = filmStorage.isFilmExist(1);

        // проверяем утверждения
        assertThat(result)
                .isNotNull() // проверяем, что объект не равен null
                .isEqualTo(true);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFilmNotExist() {
        // Подготавливаем данные для теста
        Film film = new Film();
        film.setId(1);
        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(film);

        // вызываем тестируемый метод
        Boolean result = filmStorage.isFilmExist(2);

        // проверяем утверждения
        assertThat(result)
                .isNotNull() // проверяем, что объект не равен null
                .isEqualTo(false);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindAllFilms() {
        // Подготавливаем данные для теста
        Film film = new Film();
        film.setId(1);
        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film2 = new Film();
        film2.setId(2);
        film2.setName("Пираты Карибского моря");
        film2.setDescription("Приключения Джека Воробья");
        film2.setDuration(143L);
        film2.setReleaseDate(LocalDate.of(2003, Month.AUGUST, 22));

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(film);
        filmStorage.create(film2);

        List<Film> films = new ArrayList<>();
        films.add(film);
        films.add(film2);

        // вызываем тестируемый метод
        List<Film> savedFilms = filmStorage.findAll();

        // проверяем утверждения
        assertThat(savedFilms)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(films);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testUpdateFilm() {
        // Подготавливаем данные для теста
        Film film = new Film();
        film.setId(1);
        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film2 = new Film();
        film2.setId(1);
        film2.setName("Пираты Карибского моря");
        film2.setDescription("Приключения Джека Воробья");
        film2.setDuration(143L);
        film2.setReleaseDate(LocalDate.of(2003, Month.AUGUST, 22));

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(film);

        // вызываем тестируемый метод
        Film savedFilm = filmStorage.update(film2);

        // проверяем утверждения
        assertThat(savedFilm)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(film2);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testCreateFilm() {
        // Подготавливаем данные для теста
        Film film = new Film();
        film.setId(1);
        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);

        // вызываем тестируемый метод
        Film savedFilm = filmStorage.create(film);

        // проверяем утверждения
        assertThat(savedFilm)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(film);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testSaveLike() {
        // Подготавливаем данные для теста
        Film film = new Film();
        film.setId(1);
        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);

        filmStorage.create(film);

        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);

        filmStorage.saveLike(newUser.getId(), film.getId());

        Set<Integer> likes = filmStorage.findById(1).getLikes();

        Set<Integer> result = new HashSet<>();
        result.add(1);

        // проверяем утверждения
        assertThat(likes)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(result);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testRemoveLike() {
        // Подготавливаем данные для теста
        Film film = new Film();
        film.setId(1);
        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);

        filmStorage.create(film);

        User newUser = new User(1, "user@email.ru", "vanya123", "Ivan Petrov", LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2@email.ru", "ira123", "Ira Borisova", LocalDate.of(1997, 7, 15));
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        userStorage.create(newUser2);

        filmStorage.saveLike(newUser.getId(), film.getId());
        filmStorage.saveLike(newUser2.getId(), film.getId());
        filmStorage.removeLike(newUser.getId(), film.getId());

        Set<Integer> likes = filmStorage.findById(1).getLikes();

        Set<Integer> result = new HashSet<>();
        result.add(2);

        // проверяем утверждения
        assertThat(likes)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(result);        // и сохраненного пользователя - совпадают
    }
}
