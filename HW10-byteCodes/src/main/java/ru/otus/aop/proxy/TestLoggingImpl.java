package ru.otus.aop.proxy;

/**
 * class packaging only..
 */
public class TestLoggingImpl implements TestLogging {

    @Override
    @Log
    public void calculation(Integer param) {
    }

    @Override
    public String toString() {
        return "TestLoggingImpl{}";
    }
}
