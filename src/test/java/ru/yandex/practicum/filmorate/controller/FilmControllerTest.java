package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    @Test
    void filmValidateOk() {
        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmService.createFilm(film);
        film.setId(1);
        assertEquals(film1, film, "Фильмы не совпадают");
    }

    @Test
    void filmEmptyName() {
        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

        film.setName("");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmService.createFilm(film));

        assertEquals("название не может быть пустым", ex.getMessage());
    }

    @Test
    void filmBadLongDescriptionLength() {
        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

        film.setName("Гордость и предубеждение");
        film.setDescription("«Го́рдость и предубежде́ние» (англ. Pride and Prejudice) — шестисерийный драматический " +
                "мини-сериал, вышедший в 1995 году в Великобритании на канале BBC по одноимённому роману английской " +
                "писательницы Джейн Остин, опубликованному в 1813 году.");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmService.createFilm(film));

        assertEquals("максимальная длина описания — 200 символов", ex.getMessage());
    }

    @Test
    void filmBadReleaseDate() {
        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1800, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmService.createFilm(film));

        assertEquals("дата релиза — не раньше " + LocalDate.of(1895, Month.DECEMBER, 28), ex.getMessage());
    }

    @Test
    void filmFirstReleaseDateOk() {
        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 28));

        Film film1 = filmService.createFilm(film);
        film.setId(1);
        assertEquals(film1, film, "Фильм с датой релиза 28 декабря 1895 года не создан");
    }

    @Test
    void filmWithNegativeDuration() {
        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(-300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmService.createFilm(film));

        assertEquals("продолжительность фильма должна быть положительной", ex.getMessage());
    }

    @Test
    void filmWith0Duration() {
        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(0L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmService.createFilm(film));

        assertEquals("продолжительность фильма должна быть положительной", ex.getMessage());
    }

    @Test
    void updateFilmWithoutId() {
        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин, опубликованному в 1813 году");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        ValidationException ex = assertThrows(ValidationException.class, () -> filmService.updateFilm(film));

        assertEquals("Для обновления данных фильма надо указать его Id", ex.getMessage());
    }

    @Test
    void updateFilmWithUnknownId() {
        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());

        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин, опубликованному в 1813 году");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));
        film.setId(78);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> filmService.updateFilm(film));

        assertEquals("Фильм с таким Id не существует", ex.getMessage());
    }

    @Test
    void addLikeOk() {
        UserStorage userStorage = new InMemoryUserStorage();

        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);


        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmService.createFilm(film);

        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(userStorage);

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);

        filmService.addLike(film1.getId(), user1.getId());

        assertNotNull(film1.getLikes(), "Список лайков пуст");
        assertEquals(film1.getLikes().iterator().next(), user1.getId(), "Список лайков содержит неверное значение");
        assertEquals(film1.getLikes().size(), 1, "Неверный размер списка лайков");
    }

    @Test
    void addLikeFromUnknownUser() {
        UserStorage userStorage = new InMemoryUserStorage();

        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);


        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmService.createFilm(film);

        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(userStorage);

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> filmService.addLike(film1.getId(), 5));

        assertEquals("Пользователя с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void addLikeToUnknownFilm() {
        UserStorage userStorage = new InMemoryUserStorage();

        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);


        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmService.createFilm(film);

        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(userStorage);

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> filmService.addLike(5, user1.getId()));

        assertEquals("Фильма с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void removeLikeOk() {
        UserStorage userStorage = new InMemoryUserStorage();

        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);


        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmService.createFilm(film);

        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(userStorage);

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);

        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        filmService.addLike(film1.getId(), user1.getId());
        filmService.addLike(film1.getId(), user3.getId());
        filmService.removeLike(film1.getId(), user1.getId());

        assertNotNull(film1.getLikes(), "Список лайков пуст");
        assertEquals(film1.getLikes().iterator().next(), user3.getId(), "Список лайков содержит неверное значение");
        assertEquals(film1.getLikes().size(), 1, "Неверный размер списка лайков");
    }

    @Test
    void removeLikeFromUnknownFilm() {
        UserStorage userStorage = new InMemoryUserStorage();

        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);


        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmService.createFilm(film);

        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(userStorage);

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);

        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        filmService.addLike(film1.getId(), user1.getId());
        filmService.addLike(film1.getId(), user3.getId());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> filmService.removeLike(5, user1.getId()));

        assertEquals("Фильма с Id = 5 не существует", ex.getMessage());
    }

    @Test
    void getMostLikeFilmsOk() {
        UserStorage userStorage = new InMemoryUserStorage();

        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);


        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmService.createFilm(film);

        Film film2 = new Film();
        film2.setName("Пираты Карибского моря");
        film2.setDescription("Приключения Джека Воробья");
        film2.setDuration(143L);
        film2.setReleaseDate(LocalDate.of(2003, Month.AUGUST, 22));

        Film film3 = filmService.createFilm(film2);

        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(userStorage);

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);

        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        filmService.addLike(film1.getId(), user1.getId());
        filmService.addLike(film1.getId(), user3.getId());
        filmService.addLike(film3.getId(), user3.getId());

        List<Film> popularFilms = filmService.getMostLikeFilms(1);

        assertNotNull(popularFilms, "Список фильмов пуст");
        assertEquals(popularFilms.get(0), film1, "Список фильмов содержит неверное значение");
        assertEquals(popularFilms.size(), 1, "Неверный размер списка фильмов");
    }

    @Test
    void getMostLikeFilmsWithEmptyCount() {
        UserStorage userStorage = new InMemoryUserStorage();

        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);


        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmService.createFilm(film);

        Film film2 = new Film();
        film2.setName("Пираты Карибского моря");
        film2.setDescription("Приключения Джека Воробья");
        film2.setDuration(143L);
        film2.setReleaseDate(LocalDate.of(2003, Month.AUGUST, 22));

        Film film3 = filmService.createFilm(film2);

        User user = new User("mail@ya.ru", "pika");
        UserService userService = new UserService(userStorage);

        user.setBirthday(LocalDate.of(1995, Month.SEPTEMBER, 24));
        user.setName("Пикачу");

        User user1 = userService.createUser(user);

        User user2 = new User("mail2@ya.ru", "pika2");
        user2.setBirthday(LocalDate.of(1998, Month.SEPTEMBER, 14));
        user2.setName("Пикачу2");
        User user3 = userService.createUser(user2);

        filmService.addLike(film1.getId(), user1.getId());
        filmService.addLike(film1.getId(), user3.getId());
        filmService.addLike(film3.getId(), user3.getId());

        List<Film> popularFilms = filmService.getMostLikeFilms(null);

        assertNotNull(popularFilms, "Список фильмов пуст");
        assertEquals(popularFilms.size(), 2, "Неверный размер списка фильмов");
    }

    @Test
    void findAllFilmsOk() {
        UserStorage userStorage = new InMemoryUserStorage();

        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);


        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmService.createFilm(film);

        Film film2 = new Film();
        film2.setName("Пираты Карибского моря");
        film2.setDescription("Приключения Джека Воробья");
        film2.setDuration(143L);
        film2.setReleaseDate(LocalDate.of(2003, Month.AUGUST, 22));

        Film film3 = filmService.createFilm(film2);

        List<Film> films = filmService.findAllFilms();

        assertNotNull(films, "Список фильмов пуст");
        assertEquals(films.size(), 2, "Неверный размер списка фильмов");
    }

    @Test
    void findFilmByIdOk() {
        UserStorage userStorage = new InMemoryUserStorage();

        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);


        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmService.createFilm(film);

        Film film2 = new Film();
        film2.setName("Пираты Карибского моря");
        film2.setDescription("Приключения Джека Воробья");
        film2.setDuration(143L);
        film2.setReleaseDate(LocalDate.of(2003, Month.AUGUST, 22));

        Film film3 = filmService.createFilm(film2);

        Film findFilm = filmService.findFilmById(film1.getId());

        assertEquals(findFilm, film1, "Фильмы не совпадают");
    }

    @Test
    void findFilmByIdWithUnknownId() {
        UserStorage userStorage = new InMemoryUserStorage();

        Film film = new Film();
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), userStorage);


        film.setName("Гордость и предубеждение");
        film.setDescription("по роману Дж.Остин");
        film.setDuration(300L);
        film.setReleaseDate(LocalDate.of(1995, Month.SEPTEMBER, 24));

        Film film1 = filmService.createFilm(film);

        Film film2 = new Film();
        film2.setName("Пираты Карибского моря");
        film2.setDescription("Приключения Джека Воробья");
        film2.setDuration(143L);
        film2.setReleaseDate(LocalDate.of(2003, Month.AUGUST, 22));

        Film film3 = filmService.createFilm(film2);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> filmService.findFilmById(5));

        assertEquals("Фильма с Id = 5 не существует", ex.getMessage());
    }

}