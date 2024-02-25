package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private static final Integer MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final Integer MAX_COUNT = 10;  // кол-во выдаваемых фильмов с максимальным числом лайков
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    // добавление лайка
    public void addLike(Integer filmId, Integer userId) {
        if (filmStorage.isFilmExist(filmId)) {
            Film film = filmStorage.findById(filmId);
            if (userStorage.isUserExist(userId)) {
                Set<Integer> filmLikes = film.getLikes();
                if (filmLikes != null) {
                    filmLikes.add(userId);
                } else {
                    Set<Integer> newLikes = new HashSet<>();
                    newLikes.add(userId);
                    film.setLikes(newLikes);
                }
            } else {
                log.error("Пользователя с Id = {} не существует", userId);
                throw new RuntimeException("Пользователя с Id = " + userId + " не существует");
            }
        } else {
            log.error("Фильм с Id = {} не существует", filmId);
            throw new RuntimeException("Фильма с Id = " + filmId + " не существует");
        }
    }

    // удаление лайка
    public void removeLike(Integer filmId, Integer userId) {
        if (filmStorage.isFilmExist(filmId)) {
            Film film = filmStorage.findById(filmId);
            film.getLikes().remove(userId);
        } else {
            log.error("Фильм с Id = {} не существует", filmId);
            throw new RuntimeException("Фильма с Id = " + filmId + " не существует");
        }
    }

    // получение списка фильмов с максимальным кол-вом лайков
    public List<Film> getMostLikeFilms(Integer count) {

        //  заполняем мапу id фильма - кол-во лайков
        TreeMap<Integer, Integer> filmLikes = new TreeMap<>();
        for (Film film : filmStorage.findAll()) {
            Integer likesCount = 0;
            if (film.getLikes() != null) {
                likesCount = film.getLikes().size();
            }
            filmLikes.put(film.getId(), likesCount);
        }

        // сортируем мапу по убыванию кол-ва лайков
        Map<Integer, Integer> sortedFilmLikes = filmLikes.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> -e.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        ArrayList<Film> sortedFilms = new ArrayList<>();
        if (count == null) {
            count = MAX_COUNT;
        }
        count = Math.min(count, sortedFilmLikes.size());

        // собираем список фильмов
        for (int i = 0; i < count; i++) {
            Map.Entry<Integer, Integer> firstEntry = sortedFilmLikes.entrySet().iterator().next();
            Film film = filmStorage.findById(firstEntry.getKey());
            sortedFilms.add(film);
            sortedFilmLikes.remove(firstEntry.getKey());
        }
        return sortedFilms;
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAll();
    }

    public Film findFilmById(Integer filmId) {
        if (!filmStorage.isFilmExist(filmId)) {
            log.error("Фильм с Id = {} не существует", filmId);
            throw new RuntimeException("Фильма с Id = " + filmId + " не существует");
        }
        return filmStorage.findById(filmId);
    }

    public Film createFilm(Film film) {
        validate(film);
        return filmStorage.create(film);
    }

    public Film updateFilm(Film film) {
        Integer id = film.getId();

        validate(film);
        if (id == null) {
            log.error("Id фильма не заполнен");
            throw new ValidationException("Для обновления данных фильма надо указать его Id");
        }
        if (!filmStorage.isFilmExist(id)) {
            log.error("Фильм с Id = {} не существует", id);
            throw new RuntimeException("Фильм с таким Id не существует");
        }
        return filmStorage.update(film);
    }

    private void validate(Film film) {
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Long duration = film.getDuration();
        String name = film.getName();

        if (name.isBlank()) {
            log.warn("название не может быть пустым");
            throw new ValidationException("название не может быть пустым");
        }
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            log.warn("Размер описания больше чем {} символов", MAX_DESCRIPTION_LENGTH);
            throw new ValidationException("максимальная длина описания — " + MAX_DESCRIPTION_LENGTH + " символов");
        }
        if (releaseDate != null && releaseDate.isBefore(MIN_RELEASE_DATE)) {
            log.warn("Дата релиза раньше, чем {}", MIN_RELEASE_DATE);
            throw new ValidationException("дата релиза — не раньше " + MIN_RELEASE_DATE);
        }
        if (duration != null && duration <= 0) {
            log.warn("Продолжительность фильма {} минут, а должна быть положительной", film.getDuration());
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }

    }
}
