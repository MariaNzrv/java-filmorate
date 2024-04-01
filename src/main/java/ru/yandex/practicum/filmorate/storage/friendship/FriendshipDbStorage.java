package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FriendStatusCode;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("friendshipDbStorage")
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    // добавление друга
    @Override
    public void addFriendship(Integer userId, Integer friendId, String status) {
        String sqlQuery = "insert into friendship(user_id, friend_id, status_code) " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, status);
    }

    // обновление дружбы
    @Override
    public void updateFriendship(Integer userId, Integer friendId, String status) {
        String sqlUpdate = "update friendship set status_code = ? where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlUpdate, status, userId, friendId);
    }

    // поиск дружбы
    @Override
    public Friendship getFriendship(Integer userId, Integer friendId) {
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from friendship " +
                "where user_id in (?, ?) and friend_id in (?, ?)", friendId, userId, friendId, userId);
        if (friendRows.next()) {
            return new Friendship(friendRows.getInt("user_id"), friendRows.getInt("friend_id"));
        }
        return null;
    }

    // удаление друга
    @Override
    public void removeFriendship(Integer userId, Integer friendId) {
        String sqlDeleteConnection = "delete from friendship where user_id in (?, ?) and friend_id in (?, ?)";
        jdbcTemplate.update(sqlDeleteConnection, userId, friendId, userId, friendId);
    }

    // получения списка id друзей по Id пользователя
    @Override
    public List<Integer> getFriends(Integer userId) {
        List<Integer> friends = new ArrayList<>();
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select friend_id as id " +
                        "from friendship where user_id = ? " +
                        "union select user_id as id " +
                        "from friendship where friend_id = ? and status_code = ?",
                userId, userId, String.valueOf(FriendStatusCode.CONFIRMED));
        while (friendRows.next()) {
            friends.add(friendRows.getInt("id"));
        }
        return friends;
    }
}
