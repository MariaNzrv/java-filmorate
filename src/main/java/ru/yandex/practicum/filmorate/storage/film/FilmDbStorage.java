package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (film.getMpa() != null) {
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

        return film;
    }

    // создание связей жанр-фильм
    private void updateFilmGenres(Film film) {
        for (Genre genre : film.getGenres()) {
            String sqlInsertConnection = "insert into film_genre(genre_id, film_id) values (?, ?)";
            jdbcTemplate.update(sqlInsertConnection,
                    genre.getId(),
                    film.getId());
        }
    }

    // создание связей для лайков
    private void createLikes(Film film) {
        for (Integer userId : film.getLikes()) {
            // вставляем связь фильм-пользователь
            saveLike(userId, film.getId());
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
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    id);
        } else {
            String sqlQuery = "update films set " +
                    "name = ?, description = ?, releaseDate = ?, duration = ? " +
                    "where id = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    id);
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

        return film;
    }

    @Override
    public Film findById(Integer filmId) {
        String sql = "select f.*,"
                + " r.id as rating_id, r.name as rating_name, r.description as rating_description"
                + " from films f left join rating r on f.rating_id = r.id where f.id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs), filmId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genre> getGenresByFilmId(Integer filmId) {
        String sql = "select * from genre g join film_genre fg on fg.genre_id = g.id where fg.film_id = " + filmId;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getInt("id"),
                rs.getString("name")));
    }

    // получение лайков фильма
    @Override
    public Set<Integer> getLikesByFilmId(Integer filmId) {
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
        // формируем список фильмов без жанров
        String sql = "select f.*, r.id as rating_id, r.name as rating_name, r.description as rating_description" +
                " from films f left join rating r on f.rating_id = r.id";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        Map<Integer, Film> filmMap = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select fg.film_id, fg.genre_id, g.name " +
                "from film_genre fg join genre g on g.id = fg.genre_id order by fg.film_id asc");
        while (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("genre_id"),
                    genreRows.getString("name")
            );
            filmMap.get(genreRows.getInt("film_id")).getGenres().add(genre);
        }

        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("select l.film_id, l.user_id from likes l");
        while (likesRows.next()) {
            filmMap.get(likesRows.getInt("film_id")).getLikes().add(likesRows.getInt("user_id"));
        }

        return films;
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        LocalDate releaseDate = null;
        Rating rating = null;
        if (rs.getString("rating_id") != null) {
            rating = new Rating(
                    rs.getInt("rating_id"),
                    rs.getString("rating_name"),
                    rs.getString("rating_description"));
        }
        if (rs.getDate("releaseDate") != null) {
            releaseDate = rs.getDate("releaseDate").toLocalDate();
        }
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(releaseDate);
        film.setDuration(rs.getLong("duration"));
        film.setMpa(rating);
        return film;
    }
}
