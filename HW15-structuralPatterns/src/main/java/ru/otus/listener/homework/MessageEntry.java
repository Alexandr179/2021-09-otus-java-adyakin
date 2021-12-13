package ru.otus.listener.homework;

import ru.otus.model.Message;
import java.time.LocalDateTime;

record MessageEntry(Message message, LocalDateTime createdAt) {

    public MessageEntry(Message message, LocalDateTime createdAt) {
        this.message = message;
        this.createdAt = createdAt;
    }
}
