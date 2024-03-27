package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    // получение всех жанров
    @Override
    public List<Genre> findAll() {
        String sql = "select * from genre";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(
                rs.getInt("id"),
                rs.getString("name"));
    }

    // получение жанра по id
    @Override
    public Genre findById(Integer genreId) {
        String sql = "select * from genre where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeGenre(rs), genreId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

}
