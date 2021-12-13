package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Deque<MessageEntry> history = new ArrayDeque<>();

    @Override
    public void onUpdated(Message msg) {
        history.push(new MessageEntry(msg, LocalDateTime.now()));
    }

    @Override
    public Optional<Message> findMessageById(long id) {// get copy
        Deque<MessageEntry> historyCopy = new ArrayDeque<>();
        history.forEach(messageEntry -> historyCopy.push(new MessageEntry(messageEntry.message(), messageEntry.createdAt())));
        return historyCopy.stream()
                .filter(entry -> entry.message().getId() == id)
                .findFirst()
                .map(MessageEntry::message);
    }
}
