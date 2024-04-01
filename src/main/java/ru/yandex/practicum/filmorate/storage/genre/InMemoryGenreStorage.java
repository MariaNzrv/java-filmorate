package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryGenreStorage implements GenreStorage {
    private final Map<Integer, Genre> genres = Stream.of(
            new Genre(1, "Комедия"),
            new Genre(2, "Драма"),
            new Genre(3, "Мультфильм"),
            new Genre(4, "Триллер"),
            new Genre(5, "Документальный"),
            new Genre(6, "Боевик")
    ).collect(Collectors.toMap(Genre::getId, Function.identity()));

    @Override
    public List<Genre> findAll() {
        return new ArrayList<>(genres.values());
    }

    @Override
    public Genre findById(Integer genreId) {
        return genres.get(genreId);
    }

}
