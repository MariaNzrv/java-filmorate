package ru.yandex.practicum.filmorate.storage.filmgenre;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

public class FilmGenreInMemoryStorageTest extends AbstractFilmGenreStorageTest {

    @BeforeEach
    private void init() {
        filmStorage = new InMemoryFilmStorage();
    }
}
