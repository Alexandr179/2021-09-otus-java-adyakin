package ru.otus;

import ru.otus.aop.proxy.Ioc;
import ru.otus.aop.proxy.TestLogging;
import ru.otus.aop.proxy.TestLoggingImpl;

public class Main {

    public static void main(String[] args) {
        TestLogging testLogging = new TestLoggingImpl();
        TestLogging iocClass = Ioc.createIocClass(testLogging);
        iocClass.calculation(6);
    }
}