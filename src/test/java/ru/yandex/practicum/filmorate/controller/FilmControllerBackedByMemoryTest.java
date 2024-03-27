package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.friendship.InMemoryFriendshipStorage;
import ru.yandex.practicum.filmorate.storage.genre.InMemoryGenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.InMemoryRatingStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

class FilmControllerBackedByMemoryTest extends AbstractFilmControllerTest {

    @BeforeEach
    private void init() {
        InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
        filmService = new FilmService(
                new InMemoryFilmStorage(),
                inMemoryUserStorage,
                new InMemoryRatingStorage(),
                new InMemoryGenreStorage());
        userService = new UserService(inMemoryUserStorage, new InMemoryFriendshipStorage());
    }

}