package ru.otus;

import ru.otus.aop.proxy.IocClassInterface;
import ru.otus.reflection.Log;

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
