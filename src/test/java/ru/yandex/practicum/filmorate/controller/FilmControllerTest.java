package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void filmValidateOk() {
        Film film = new Film();
        FilmController filmController = new FilmController();

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmController.create(film);
        film.setId(1);
        assertEquals(film1, film, "Фильмы не совпадают");
    }

    @Test
    void filmEmptyName(){
        Film film = new Film();
        FilmController filmController = new FilmController();

        film.setName("");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));

        assertEquals("название не может быть пустым", ex.getMessage());
    }

    @Test
    void filmBadLongDescriptionLength(){
        Film film = new Film();
        FilmController filmController = new FilmController();

        film.setName("Гордость и предубеждение");
        film.setDescription("«Го́рдость и предубежде́ние» (англ. Pride and Prejudice) — шестисерийный драматический " +
                "мини-сериал, вышедший в 1995 году в Великобритании на канале BBC по одноимённому роману английской " +
                "писательницы Джейн Остин, опубликованному в 1813 году.");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));

        assertEquals("максимальная длина описания — 200 символов", ex.getMessage());
    }

    @Test
    void filmBadReleaseDate(){
        Film film = new Film();
        FilmController filmController = new FilmController();

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1800, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));

        assertEquals("дата релиза — не раньше 28 декабря 1895 года", ex.getMessage());
    }

    @Test
    void filmFirstReleaseDateOk(){
        Film film = new Film();
        FilmController filmController = new FilmController();

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 28));

        Film film1 = filmController.create(film);
        film.setId(1);
        assertEquals(film1, film, "Фильм с датой релиза 28 декабря 1895 года не создан");
    }

    @Test
    void filmWithNegativeDuration(){
        Film film = new Film();
        FilmController filmController = new FilmController();

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(-300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));

        assertEquals("продолжительность фильма должна быть положительной", ex.getMessage());
    }

    @Test
    void filmWith0Duration(){
        Film film = new Film();
        FilmController filmController = new FilmController();

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(0L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film));

        assertEquals("продолжительность фильма должна быть положительной", ex.getMessage());
    }

    @Test
    void createFilmWithExistId(){
        Film film = new Film();
        FilmController filmController = new FilmController();

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmController.create(film);

        Film film2 = new Film();
        film2.setId(1);
        film2.setName("Гордость и предубеждение");
        film2.setDescription("по роману Дж.Остин");
        film2.setDuration(161L);
        film2.setReleaseDate(LocalDate.of(2005, Month.JULY, 25));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.create(film2));

        assertEquals("Фильм уже существует", ex.getMessage());
    }

    @Test
    void updateFilmWithoutId(){
        Film film = new Film();
        FilmController filmController = new FilmController();

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин, опубликованному в 1813 году");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.update(film));

        assertEquals("Для обновления данных фильма надо указать его Id", ex.getMessage());
    }

    @Test
    void updateFilmWithUnknownId(){
        Film film = new Film();
        FilmController filmController = new FilmController();

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин, опубликованному в 1813 году");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));
        film.setId(78);

        ValidationException ex = assertThrows(ValidationException.class, () -> filmController.update(film));

        assertEquals("Фильм с таким Id не существует", ex.getMessage());
    }

}