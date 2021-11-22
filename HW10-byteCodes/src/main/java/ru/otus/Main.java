package ru.otus;

import ru.otus.aop.proxy.Ioc;
import ru.otus.aop.proxy.IocClassInterface;
import ru.otus.aop.proxy.TestLogging;

/**
 * WITHOUT suppress SLF4J-warnings (ТЗ: ..явного вызова логирования быть не должно)
 */

public class Main {

    public static void main(String[] args) {
        IocClassInterface iocClass = Ioc.createIocClass(new TestLogging());
        // call method by <iocClass>
        iocClass.calculation(6);
    }
}