SELECT * FROM films f;

SELECT *
FROM films f
WHERE f.film_id = 1;

SELECT g.genre
FROM films_genres f
JOIN genres g
    ON g.genre_id = f.genre_id
WHERE f.film_id = 1;

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
LIMIT 3;

SELECT * FROM users;

SELECT *
FROM users u
WHERE u.user_id = 2;

SELECT u.*
FROM friends f
JOIN users u
    ON f.friend_id = u.user_id
WHERE f.user_id = 1
    AND status = 'true';

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
