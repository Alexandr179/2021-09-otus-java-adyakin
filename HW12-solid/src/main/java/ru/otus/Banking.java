package ru.otus;

import java.util.Map;

public interface Banking {

    void addToSafe(Map<Note, Integer> amountPutted);

    void takeFromSafe(Integer sum) throws IllegalAccessException;

    Map<Note, Integer> lookAtMapSafe();

    Integer lookAtIntSafe();
}
