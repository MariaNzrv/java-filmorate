package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.*;

@Component
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        RatingStorage ratingStorage = new RatingDbStorage(jdbcTemplate);

        if (film.getMpa() != null) {
            if (ratingStorage.findById(film.getMpa().getId()) == null) {
                log.error("Рейтинг с идентификатором {} не найден.", film.getMpa().getId());
                throw new ValidationException("Рейтинг с идентификатором " + film.getMpa().getId() + " не найден.");
            }
            String sqlQueryWithRating = "insert into films(name, description, releaseDate, duration, rating_id) values (?, ?, ?, ?, ?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQueryWithRating, new String[]{"id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setLong(4, film.getDuration());
                stmt.setInt(5, film.getMpa().getId());
                return stmt;
            }, keyHolder);
        } else {
            String sqlQuery = "insert into films(name, description, releaseDate, duration) values (?, ?, ?, ?)";
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
                stmt.setLong(4, film.getDuration());
                return stmt;
            }, keyHolder);
        }

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        updateFilmGenres(film);
        createLikes(film);

        log.info("Создан фильм с Id: '{}'", film.getId());
        return film;
    }

    // создание связей жанр-фильм
    private void updateFilmGenres(Film film) {
        if (!film.getGenres().isEmpty()) {
            GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);
            for (Genre genre : film.getGenres()) {
                // ищем жанр по id
                if (genreStorage.findById(genre.getId()) != null) {
                    // вставляем связь жанр-фильм
                    String sqlInsertConnection = "insert into film_genre(genre_id, film_id) values (?, ?)";
                    jdbcTemplate.update(sqlInsertConnection,
                            genre.getId(),
                            film.getId());
                } else {
                    log.error("Жанр '{}' не найден в системе", genre.getId());
                    throw new RuntimeException("Жанр '" + genre.getId() + "' не найден в системе");
                }
            }
        }
    }

    // создание связей для лайков
    private void createLikes(Film film) {
        if (!film.getLikes().isEmpty()) {
            UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
            for (Integer userId : film.getLikes()) {
                // ищем пользователя по Id
                if (userDbStorage.isUserExist(userId)) {
                    // вставляем связь фильм-пользователь
                    saveLike(userId, film.getId());
                } else {
                    log.error("Пользователь с идентификатором {} не найден.", userId);
                    throw new RuntimeException("Пользователь с идентификатором " + userId + " не найден.");
                }
            }
        }
    }

    // сохранение лайка в БД
    @Override
    public void saveLike(Integer userId, Integer filmId) {
        String sqlInsertConnection = "insert into likes(user_id, film_id) values (?, ?)";
        jdbcTemplate.update(sqlInsertConnection,
                userId,
                filmId);
    }

    // удаление лайка из БД
    @Override
    public void removeLike(Integer userId, Integer filmId) {
        String sqlDeleteLikes = "delete from likes where user_id = ? and film_id = ?";
        jdbcTemplate.update(sqlDeleteLikes, userId, filmId);
    }


    @Override
    public Boolean isFilmExist(Integer filmId) {
        String sqlQuery = "select * from films where id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        return filmRows.next();
    }

    @Override
    public Film update(Film film) {
        Integer id = film.getId();
        if (film.getMpa() != null) {
            String sqlQuery = "update films set " +
                    "name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? " +
                    "where id = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa().getId()
                    , id);
        } else {
            String sqlQuery = "update films set " +
                    "name = ?, description = ?, releaseDate = ?, duration = ? " +
                    "where id = ?";
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , id);
        }

        // удаляем все связи жанр-фильм
        String sqlDeleteConnection = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlDeleteConnection, id);

        //удаляем все лайки
        String sqlDeleteLikes = "delete from likes where film_id = ?";
        jdbcTemplate.update(sqlDeleteLikes, id);

        // вставляем заново связи жанр-фильм
        updateFilmGenres(film);
        createLikes(film);

        log.info("Обновлен фильм с Id: '{}'", id);
        return film;
    }

    @Override
    public Film findById(Integer filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", filmId);
        if (filmRows.next()) {
            LocalDate releaseDate = null;
            if (filmRows.getDate("releaseDate") != null) {
                releaseDate = filmRows.getDate("releaseDate").toLocalDate();
            }
            RatingStorage ratingStorage = new RatingDbStorage(jdbcTemplate);
            GenreStorage genreStorage = new GenreDbStorage(jdbcTemplate);
            Film film = new Film();
            film.setId(filmRows.getInt("id"));
            film.setName(filmRows.getString("name"));
            film.setDescription(filmRows.getString("description"));
            film.setReleaseDate(releaseDate);
            film.setDuration(filmRows.getLong("duration"));
            film.setMpa(ratingStorage.findById(filmRows.getInt("rating_id")));
            film.setGenres(genreStorage.getGenresByFilmId(filmId));
            film.setLikes(getLikesByFilmId(filmId));
            log.info("Найден фильм: {} {}", film.getId(), film.getName());

            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден.", filmId);
            return null;
        }
    }

    // получение лайков фильма
    private Set<Integer> getLikesByFilmId(Integer filmId) {
        Set<Integer> likes = new HashSet<>();
        SqlRowSet connectionRows = jdbcTemplate.queryForRowSet("select * from likes where film_id = ?", filmId);
        while (connectionRows.next()) {
            Integer userId = connectionRows.getInt("user_id");
            UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
            if (userDbStorage.isUserExist(userId)) {
                likes.add(userId);
            }
        }
        return likes;
    }

    @Override
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select id from films order by id asc");
        while (filmRows.next()) {
            films.add(findById(filmRows.getInt("id")));
        }
        return films;
    }
}
