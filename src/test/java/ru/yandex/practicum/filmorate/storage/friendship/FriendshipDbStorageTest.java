package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class FriendshipDbStorageTest extends AbstractFriendshipStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    private void init() {
        friendshipStorage = new FriendshipDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate);
    }
}
