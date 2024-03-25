DELETE
FROM genre;
---- Заполняем таблицу с жанрами
INSERT INTO genre (name)
VALUES ('Комедия');
INSERT INTO genre (name)
VALUES ('Драма');
INSERT INTO genre (name)
VALUES ('Мультфильм');
INSERT INTO genre (name)
VALUES ('Триллер');
INSERT INTO genre (name)
VALUES ('Документальный');
INSERT INTO genre (name)
VALUES ('Боевик');
COMMIT;

DELETE
FROM rating;
------- Заполняем таблицу с рейтингами
INSERT INTO rating (name, description)
VALUES ('G', 'Нет возрастных ограничений');
INSERT INTO rating (name, description)
VALUES ('PG', 'Рекомендуется присутствие родителей');
INSERT INTO rating (name, description)
VALUES ('PG-13', 'Детям до 13 лет просмотр не желателен');
INSERT INTO rating (name, description)
VALUES ('R', 'Лицам до 17 лет обязательно присутствие взрослого');
INSERT INTO rating (name, description)
VALUES ('NC-17', 'Лицам до 18 лет просмотр запрещен');
COMMIT;

DELETE
FROM friend_status;
----- Заполняем таблицу статуса дружбы
INSERT INTO friend_status (status_code)
VALUES ('CONFIRMED');
INSERT INTO friend_status (status_code)
VALUES ('UNCONFIRMED');
COMMIT;