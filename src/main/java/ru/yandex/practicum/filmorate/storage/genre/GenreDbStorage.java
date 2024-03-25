package ru.yandex.practicum.filmorate.storage.genre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenreName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {
    private final Logger log = LoggerFactory.getLogger(GenreDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // получение всех жанров
    @Override
    public List<Genre> findAll() {
        String sql = "select * from genre";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(
                rs.getInt("id"),
                GenreName.valueOf(rs.getString("name")));
    }

    // получение жанра по id
    @Override
    public Genre findById(Integer genreId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from genre where id = ?", genreId);
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("id"),
                    GenreName.valueOf(genreRows.getString("name")));
            log.info("Найден жанр: {} {}", genre.getId(), genre.getName());

            return genre;
        } else {
            log.info("Жанр с идентификатором {} не найден.", genreId);
            return null;
        }
    }

    // получение списка жанров фильма
    @Override
    public List<Genre> getGenresByFilmId(Integer filmId) {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet connectionRows = jdbcTemplate.queryForRowSet("select * from film_genre where film_id = ?", filmId);
        while (connectionRows.next()) {
            Integer genreId = connectionRows.getInt("genre_id");
            if (findById(genreId) != null) {
                genres.add(findById(genreId));
            }
        }
        return genres;
    }

    @Override
    public Boolean isGenreExist(Integer genreId) {
        String sqlQuery = "select * from genre where id = ?";
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet(sqlQuery, genreId);
        return genresRows.next();
    }
}
