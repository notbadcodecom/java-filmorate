# База данных для java-filmorate

## ER-диаграмма
![Таблицы films, users](https://github.com/notbadcodecom/java-filmorate/blob/db_description/db_description/img/filmorate.png?raw=true)

## SQL-запросы
### Получение всех фильмов
```
SELECT * FROM films;
```
![films](https://github.com/notbadcodecom/java-filmorate/blob/db_description/db_description/img/get_all_films.png?raw=true)

### Получение фильма по id
```
SELECT *
FROM films f
WHERE f.film_id = 1;
```
![film](https://github.com/notbadcodecom/java-filmorate/blob/db_description/db_description/img/get_film_by_id.png?raw=true)

### Получение жанров фильма по его id
```
SELECT g.genre
FROM films_genres f
JOIN genres g
    ON g.genre_id = f.genre_id
WHERE f.film_id = 1;
```
![genre](https://github.com/notbadcodecom/java-filmorate/blob/db_description/db_description/img/get_genre.png?raw=true)

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
![popular film](https://github.com/notbadcodecom/java-filmorate/blob/db_description/db_description/img/get_films_by_users_rating.png?raw=true)

### Получение всех пользователей
```
SELECT * FROM users;
```
![users](https://github.com/notbadcodecom/java-filmorate/blob/db_description/db_description/img/get_all_users.png?raw=true)

### Получение пользователя по id
```
SELECT *
FROM users u
WHERE u.user_id = 2;
```
![user](https://github.com/notbadcodecom/java-filmorate/blob/db_description/db_description/img/get_user_by_id.png?raw=true)

### Получение друзей пользователя по id
```
SELECT u.*
FROM friends f
JOIN users u
    ON f.friend_id = u.user_id
WHERE f.user_id = 1
    AND status = 'true';
```
![friends](https://github.com/notbadcodecom/java-filmorate/blob/db_description/db_description/img/get_friends.png?raw=true)

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
![friends](https://github.com/notbadcodecom/java-filmorate/blob/db_description/db_description/img/get_common_friends.png?raw=true)