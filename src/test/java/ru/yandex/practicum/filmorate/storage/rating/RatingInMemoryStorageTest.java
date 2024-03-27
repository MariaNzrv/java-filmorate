package ru.yandex.practicum.filmorate.storage.rating;

import org.junit.jupiter.api.BeforeEach;

public class RatingInMemoryStorageTest extends AbstractRatingStorageTest {

    @BeforeEach
    private void init() {
        ratingStorage = new InMemoryRatingStorage();
    }
}
