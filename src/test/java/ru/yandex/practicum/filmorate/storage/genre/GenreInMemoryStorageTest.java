package ru.yandex.practicum.filmorate.storage.genre;

import org.junit.jupiter.api.BeforeEach;

public class GenreInMemoryStorageTest extends AbstractGenreStorageTest {

    @BeforeEach
    private void init() {
        genreStorage = new InMemoryGenreStorage();
    }

}
