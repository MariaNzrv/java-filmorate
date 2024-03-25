package ru.yandex.practicum.filmorate.storage.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("friendshipDbStorage")
public class FriendshipDbStorage implements FriendshipStorage {

    private final Logger log = LoggerFactory.getLogger(FriendshipDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // добавление друга
    @Override
    public void addFriendship(Integer userId, Integer friendId) {
        String sqlQuery = "insert into friendship(user_id, friend_id, status_code_id) " +
                "values (?, ?, ?)";

        //проверяем, есть ли обратная запись, где у друга в друзьях числится пользователь
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from friendship " +
                "where user_id = ? and friend_id = ?", friendId, userId);
        if (friendRows.next()) {
            // если да, то дружба подтверждена
            // вставляем новую связь с подтвержденным статусом
            jdbcTemplate.update(sqlQuery,
                    userId,
                    friendId,
                    getStatusCodeId("CONFIRMED"));

            // обновляем статус дружбы у существующей связи
            String sqlUpdate = "update friendship set status_code_id = ? where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sqlUpdate, getStatusCodeId("CONFIRMED"), friendId, userId);

            log.info("Подтверждена дружба пользователей с id: {} {}", userId, friendId);
        } else {
            // иначе, вставляем новую связь с неподтвержденным статусом
            jdbcTemplate.update(sqlQuery, userId, friendId, getStatusCodeId("UNCONFIRMED"));

            log.info("Пользователю с id = {} добавлен друг с id = {}", userId, friendId);
        }

    }

    private Integer getStatusCodeId(String statusCode) {
        SqlRowSet statusRows = jdbcTemplate.queryForRowSet("select id from friend_status where status_code = ?", statusCode);
        if (statusRows.next()) {
            return statusRows.getInt("id");
        } else {
            return null;
        }
    }

    // удаление друга
    @Override
    public void removeFriendship(Integer userId, Integer friendId) {
        String sqlDeleteConnection = "delete from friendship where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlDeleteConnection, userId, friendId);
        jdbcTemplate.update(sqlDeleteConnection, friendId, userId);

        log.info("Пользователи с id: {} {}, больше не являются друзьями", userId, friendId);
    }

    // получения списка id друзей по Id пользователя
    @Override
    public List<Integer> getFriends(Integer userId) {
        List<Integer> friends = new ArrayList<>();
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select friend_id from friendship where user_id = ?",
                userId);
        while (friendRows.next()) {
            friends.add(friendRows.getInt("friend_id"));
        }
        return friends;
    }
}
