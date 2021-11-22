package ru.otus.aop.proxy;

import ru.otus.reflection.Log;

/**
 * class packaging only..
 */

public class TestLogging implements IocClassInterface {

    @Override
    @Log
    public void calculation(Integer param) {
    }

    @Override
    public String toString() {
        return "TestLoggingImpl{}";
    }
}
