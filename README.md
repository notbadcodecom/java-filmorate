# Filmorate project

## Описание
Бэкэнд (rest api) соцсети, в котором пользователи могут:
- оценивать фильмы для составления рейтинга;
- писать отзывы фильмам;
- оценивать отзывы;
- получать рейтинг популярных фильмов;
- получать фильмы по режиссерам;
- искать фильмы;
- получать общие фильмы друзей;
- добавлять пользователей в друзья;
- получать рекомендованные пользователю фильмы по оценкам;
- просматривать ленту событий пользователя.

## Технологии
- Java 11, Lombok;
- Spring Boot;
- JDBC, SQL, H2;
- Maven, Swagger, Junit;
- Postman.

## Командная работа, реализованный функционал 

- [Александр Васильев:](https://github.com/notbadcodecom)
  - "Пользователи", 
  - "Фильмы, рейтинги MPA, жанры",
  - "Добавление режиссёров в фильмы",
  - "Вывод самых популярных фильмов по жанру и годам".
- [Александр Кокорин:](https://github.com/Akokorin89)
  - "Рекомендации", 
  - "Удаление фильмов и пользователей".
- [Александр Болонкин:](https://github.com/BolonkinAleksandr)
  - "Отзывы".
- [Александр Назаров:](https://github.com/9815444)
  - "Лента событий".
- [Владимир Веретенников:](https://github.com/TheRevoIt)
  - "Поиск",
  - "Общие фильмы".

## Запуск и тестирование проекта
1. git clone
2. mvn package
3. docker build -t filmorate .
4. docker run --name filmorate -it -p 8080:8080 filmorate

Список эндпоинтов доступен в swagger после запуска приложения по ссылке:
http://localhost:8080/swagger.html

Для тестирования приложения можно воспользоваться коллекцией Postman в одноименной папке.
