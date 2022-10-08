package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventOperation;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.EventType;

import java.util.List;

@Slf4j
@Service
public class EventService {

    private final EventStorage eventStorage;

    public EventService(EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    public void saveEvent(long userId, long entityId, EventType eventType, EventOperation Operation) {
        eventStorage.saveEvent(userId, entityId, eventType, Operation);
    }

    public List<Event> getEvents(long userId) {
        return eventStorage.getEvents(userId);
    }
}
