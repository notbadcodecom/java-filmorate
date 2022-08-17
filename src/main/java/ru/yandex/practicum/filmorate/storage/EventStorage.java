package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {
    void saveEvent(long userId, long entityId, EventType eventType, EventOperation Operation);

    List<Event> getEvents(long userId);
}
