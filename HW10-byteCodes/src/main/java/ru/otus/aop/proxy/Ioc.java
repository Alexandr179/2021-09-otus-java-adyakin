package ru.otus.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static ru.otus.reflection.Reflection.annotationOnMethodPresent;
import static ru.otus.reflection.Reflection.reflectionLogProcess;

public class Ioc {

    private Ioc() {
    }

    public static IocClassInterface createIocClass(TestLogging testLogging) {
        InvocationHandler handler = new SimpleInvocationHandler(testLogging);
        return (IocClassInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{IocClassInterface.class}, handler);
    }

    static class SimpleInvocationHandler implements InvocationHandler {
        private final IocClassInterface myClass;

        SimpleInvocationHandler(IocClassInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // проверка над методом @Log и вывод в консоль:  executed method: calculation, param: 6
            if (!annotationOnMethodPresent) reflectionLogProcess(method, args);
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "SimpleInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
