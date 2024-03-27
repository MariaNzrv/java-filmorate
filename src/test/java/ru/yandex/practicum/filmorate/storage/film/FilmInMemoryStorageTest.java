package ru.yandex.practicum.filmorate.storage.film;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

public class FilmInMemoryStorageTest extends AbstractFilmStorageTest {

    @BeforeEach
    private void init() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
    }

}
