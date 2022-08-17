CREATE TABLE IF NOT EXISTS mpa (
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(16) NOT NULL
);

CREATE TABLE IF NOT EXISTS genres (
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  TEXT NOT NULL,
    release_date DATE NOT NULL,
    duration     BIGINT NOT NULL,
    mpa_id       BIGINT REFERENCES mpa(id) ON DELETE NO ACTION,
    genre_id     BIGINT REFERENCES genres(id) ON DELETE SET NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS film_id_uniq_index
    ON films (id);

CREATE TABLE IF NOT EXISTS films_genres (
    film_id  INTEGER REFERENCES films (id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genres (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login    VARCHAR(32) UNIQUE NOT NULL,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(64) UNIQUE NOT NULL,
    birthday DATE
);

CREATE UNIQUE INDEX IF NOT EXISTS user_id_uniq_index
    ON users (id);

CREATE TABLE IF NOT EXISTS films_ratings (
    film_id BIGINT REFERENCES films (id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    UNIQUE  (film_id, user_id)
);

CREATE TABLE IF NOT EXISTS friends (
    user_id   BIGINT REFERENCES users (id) ON DELETE CASCADE,
    friend_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
    status    VARCHAR NOT NULL,
    UNIQUE    (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS reviews
(
    review_id   bigint AUTO_INCREMENT PRIMARY KEY,
    content     varchar(400),
    is_positive bool,
    user_id     int REFERENCES users (id) ON DELETE CASCADE,
    film_id     int REFERENCES films (id) ON DELETE CASCADE,
    useful      int DEFAULT 0
);

CREATE TABLE IF NOT EXISTS reactions
(
    reaction_id int AUTO_INCREMENT PRIMARY KEY,
    name        varchar(100),
    mark        int
);

CREATE TABLE IF NOT EXISTS reviews_reactions
(
    review_id   bigint REFERENCES reviews (review_id) on delete cascade ,
    user_id     bigint REFERENCES users (id) ON DELETE CASCADE,
    reaction_id int REFERENCES reactions (reaction_id),
    PRIMARY KEY (review_id, user_id)
);