package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    @Autowired
    private final GenreStorage genreStorage;

    public List<Genre> findAllGenres() {
        return genreStorage.findAll();
    }

    public Genre findGenreById(Integer genreId) {
        Genre genre = genreStorage.findById(genreId);
        if (genre == null) {
            log.error("Жанр с Id = {} не существует", genreId);
            throw new RuntimeException("Жанр с Id = " + genreId + " не существует");
        }

        return genre;
    }
}
