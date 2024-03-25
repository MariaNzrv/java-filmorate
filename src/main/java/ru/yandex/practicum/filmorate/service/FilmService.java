package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    // добавление лайка
    public void addLike(Integer filmId, Integer userId) {
        if (filmStorage.isFilmExist(filmId)) {
            Film film = filmStorage.findById(filmId);
            if (userStorage.isUserExist(userId)) {
                Set<Integer> filmLikes = film.getLikes();
                filmLikes.add(userId);
                filmStorage.saveLike(userId, filmId);
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
            if (userStorage.isUserExist(userId)) {
                film.getLikes().remove(userId);
                filmStorage.removeLike(userId, filmId);
            } else {
                log.error("Пользователя с Id = {} не существует", userId);
                throw new RuntimeException("Пользователя с Id = " + userId + " не существует");
            }
        } else {
            log.error("Фильм с Id = {} не существует", filmId);
            throw new RuntimeException("Фильма с Id = " + filmId + " не существует");
        }
    }

    // получение списка фильмов с максимальным кол-вом лайков
    public List<Film> getMostLikeFilms(Integer count) {

        //  создаем отсортированный по убыванию кол-ва лайков список фильмов
        Comparator<Film> filmComparator = Comparator.comparingInt(f -> f.getLikes().size());
        TreeSet<Film> filmLikes2 = new TreeSet<>(filmComparator.thenComparing(Film::hashCode).reversed());
        filmLikes2.addAll(filmStorage.findAll());

        //берем из этого списка нужное кол-во фильмов
        if (count == null) {
            count = MAX_COUNT;
        }
        count = Math.min(count, filmLikes2.size());
        return filmLikes2.stream().limit(count).collect(Collectors.toList());
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
