package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("eventStorage")
public class EventStorageDb implements EventStorage {

    private final JdbcTemplate jdbcTemplate;

    public EventStorageDb(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveEvent(long userId, long entityId, EventType eventType, EventOperation Operation) {
        String sqlQuery = "INSERT INTO EVENTS (user_id, ENTITY_ID, EVENT_TYPE, Operation) VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(sqlQuery, userId, entityId, eventType.name(), Operation.name());
    }

    @Override
    public List<Event> getEvents(long userId) {
        String sqlQuery = "SELECT EVENT_ID, TIMESTAMP, USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID " +
                          "FROM EVENTS " +
                          "WHERE USER_ID = ?;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToEvent, userId);
    }

    private Event mapRowToEvent(ResultSet resultSet, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(resultSet.getLong("EVENT_ID"))
                .timestamp(resultSet.getTimestamp("TIMESTAMP").getTime())
                .userId(resultSet.getLong("USER_ID"))
                .eventType(EventType.valueOf(resultSet.getString("EVENT_TYPE")))
                .operation(EventOperation.valueOf(resultSet.getString("OPERATION")))
                .entityId(resultSet.getLong("ENTITY_ID"))
                .build();
    }
}
