package ru.otus.aop.proxy;

import ru.otus.TestLogging;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import static ru.otus.reflection.Reflection.reflectionLogProcess;

public class Ioc {

    private Ioc() {
    }

    public static IocClassInterface createIocClass() {
        InvocationHandler handler = new SimpleInvocationHandler(new TestLogging());
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
            // проверка над методом @Log и вывод (ifPresent) - в консоль:   executed method: calculation, param: 6
            reflectionLogProcess(proxy, method, args);
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
