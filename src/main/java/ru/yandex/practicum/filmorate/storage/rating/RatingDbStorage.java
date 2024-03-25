package ru.yandex.practicum.filmorate.storage.rating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RatingDbStorage implements RatingStorage {
    private final Logger log = LoggerFactory.getLogger(RatingDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // получение всех жанров
    @Override
    public List<Rating> findAll() {
        String sql = "select * from rating";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        return new Rating(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"));
    }

    // получение жанра по id
    @Override
    public Rating findById(Integer ratingId) {
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("select * from rating where id = ?", ratingId);
        if (ratingRows.next()) {
            Rating rating = new Rating(
                    ratingRows.getInt("id"),
                    ratingRows.getString("name"),
                    ratingRows.getString("description"));
            log.info("Найден рейтинг: {} {}", rating.getId(), rating.getName());

            return rating;
        } else {
            log.info("Рейтинг с идентификатором {} не найден.", ratingId);
            return null;
        }
    }

    @Override
    public Boolean isRatingExist(Integer ratingId) {
        String sqlQuery = "select * from rating where id = ?";
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(sqlQuery, ratingId);
        return ratingRows.next();
    }
}
