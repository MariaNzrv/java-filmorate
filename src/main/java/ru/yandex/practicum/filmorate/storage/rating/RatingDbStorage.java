package ru.yandex.practicum.filmorate.storage.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

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
        String sql = "select * from rating where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeRating(rs), ratingId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
