package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class FilmControllerBackedByDbTest extends AbstractFilmControllerTest {
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    private void init() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        filmService = new FilmService(
                new FilmDbStorage(jdbcTemplate),
                userStorage,
                new RatingDbStorage(jdbcTemplate),
                new GenreDbStorage(jdbcTemplate));
        userService = new UserService(
                userStorage,
                new FriendshipDbStorage(jdbcTemplate));
    }
}