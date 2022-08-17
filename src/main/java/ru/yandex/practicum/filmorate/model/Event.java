package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.EventOperation;
import ru.yandex.practicum.filmorate.storage.EventType;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class Event {
    private long eventId;
    private LocalDateTime timeStamp;
    private long userId;
    private EventType eventType;
    private EventOperation operation;
    private long entityId;
}
