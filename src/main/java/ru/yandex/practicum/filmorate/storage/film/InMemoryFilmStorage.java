package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final HashMap<Integer, Film>  films = new HashMap<>();  // хранение фильмов в системе
    private Integer idCounter = 1; //счетчик id

    @Override
    public Film create (Film film) {
        Integer id = getUniqueId();

        film.setId(id);
        films.put(id, film);
        log.info("Создан фильм с Id: '{}'", id);
        return films.get(id);
    }

    @Override
    public Film update (Film film) {
        Integer id = film.getId();

        films.put(id, film);
        log.info("Обновлен фильм с Id: '{}'", id);
        return films.get(id);
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(Integer filmId){
        return films.get(filmId);
    }

    @Override
    public Boolean isFilmExist(Integer filmId){
        return films.containsKey(filmId);
    }



    private Integer getUniqueId() {
        // вычисление уникального Id
        Integer result = idCounter;
        idCounter++;
        return result;
    }

}
