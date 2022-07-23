# База данных для java-filmorate

## ER-диаграмма
![Таблицы films, users](/img/filmorate.png)

## SQL-запросы
### Получение всех фильмов
```
SELECT * FROM films;
```
![films](/img/get_all_films.png)

### Получение фильма по id
```
SELECT *
FROM films f
WHERE f.film_id = 1;
```
![film](/img/get_film_by_id.png)

### Получение жанров фильма по его id
```
SELECT g.genre
FROM films_genres f
JOIN genres g
    ON g.genre_id = f.genre_id
WHERE f.film_id = 1;
```
![genre](/img/get_genre.png)

### Получение n популярных фильмов
```
SELECT f.*,
    r.raiting
FROM films f
JOIN (
    SELECT film_id,
        COUNT(user_id) raiting
    FROM films_ratings
    GROUP BY film_id
) r ON f.film_id =  r.film_id
ORDER BY r.raiting DESC
LIMIT 3; --{n}
```
![popular film](/img/get_films_by_users_rating.png)

### Получение всех пользователей
```
SELECT * FROM users;
```
![users](/img/get_all_users.png)

### Получение пользователя по id
```
SELECT *
FROM users u
WHERE u.user_id = 2;
```
![user](/img/get_user_by_id.png)

### Получение друзей пользователя по id
```
SELECT u.*
FROM friends f
JOIN users u
    ON f.friend_id = u.user_id
WHERE f.user_id = 1
    AND status = 'true';
```
![friends](/img/get_friends.png)

### Получение общих друзей у двух пользователей по их id
```
SELECT u.*
FROM friends f
JOIN users u
    ON f.friend_id = u.user_id
WHERE f.user_id = 1
    AND status = 'true'
UNION
SELECT u.*
FROM friends f
JOIN users u
    ON f.friend_id = u.user_id
WHERE f.user_id = 2
    AND status = 'true';
```
![friends](/img/get_common_friends.png)