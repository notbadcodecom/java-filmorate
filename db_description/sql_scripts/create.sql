DROP DATABASE filmorate_test;

CREATE DATABASE filmorate_test;

\connect filmorate_test;

CREATE TYPE rating_mpa AS ENUM('G', 'PG', 'PG-13', 'R', 'NC-17');

CREATE TABLE films(
    film_id      serial primary key,
    name         varchar(255) not null,
    description  text not null,
    release_date date not null,
    duration     bigint not null,
    rating_mpa   rating_mpa
);

CREATE TABLE genres(
    genre_id serial primary key,
    genre    varchar(255) not null
);

CREATE TABLE films_genres(
    film_id  integer references films (film_id),
    genre_id integer references genres (genre_id)
);

CREATE TABLE users(
    user_id  serial primary key,
    login    varchar(255) not null,
    name     varchar(255),
    email    varchar(255) not null,
    birthday date
);

CREATE TABLE films_ratings(
    film_id integer references films (film_id),
    user_id integer references users (user_id)
);

CREATE TABLE friends(
    user_id   integer references users (user_id),
    friend_id integer references users (user_id),
    status boolean not null
);
