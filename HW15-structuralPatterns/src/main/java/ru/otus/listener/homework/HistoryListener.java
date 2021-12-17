package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, MessageEntry> history = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        Message msgCopy = msg.toBuilder().field13(msg.getField13()).field10(msg.getField10()).build();
        history.put(msg.getId(), new MessageEntry(msgCopy, LocalDateTime.now()));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.of(history.get(id).message());
    }
}
