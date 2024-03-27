# java-filmorate
Template repository for Filmorate project.


![DB Schema.](/filmorate.svg)


Получение всех фильмов:

```
SELECT *
FROM films;
```

Получение фильма по Id:

```
SELECT *
FROM films
WHERE id = 'Id';
```

Получение всех пользователей:

```
SELECT *
FROM users;
```

Получение пользователя по Id:

```
SELECT *
FROM users
WHERE id = 'Id';
```

Получение списка друзей:

```
SELECT friend_id AS friend
FROM friendship
WHERE user_id = 'Id';
```

Получение списка общих друзей:

```
SELECT friend_id AS friend
FROM friendship
WHERE user_id = 'Id_1' AND
      friend_id IN (SELECT friend_id AS friend
                    FROM friendship
                    WHERE user_id = 'Id_2');
```

Получение 10 фильмов с максимальным кол-вом лайков:

```
SELECT *
FROM films
WHERE id IN (SELECT film_id
FROM like
GROUP BY film_id
ORDER BY COUNT(film_id) DESC
LIMIT 10);
```
