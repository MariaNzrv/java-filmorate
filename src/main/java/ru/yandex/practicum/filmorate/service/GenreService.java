package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> findAllGenres() {
        return genreStorage.findAll();
    }

    public Genre findGenreById(Integer genreId) {
        if (!genreStorage.isGenreExist(genreId)) {
            log.error("Жанр с Id = {} не существует", genreId);
            throw new RuntimeException("Жанр с Id = " + genreId + " не существует");
        }

        return genreStorage.findById(genreId);
    }
}
