package ru.otus.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Ioc {

    private Ioc() {
    }

    public static TestLogging createIocClass(TestLogging testLogging) {
        InvocationHandler handler = new SimpleInvocationHandler(testLogging);
        return (TestLogging) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    static class SimpleInvocationHandler implements InvocationHandler {

        private final TestLogging myClass;
        private List<Method> logMethods;

        SimpleInvocationHandler(TestLogging myClass) {
            this.myClass = myClass;
            this.logMethods = new ArrayList<>();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logMethods.size() == 0) logMethods = Arrays.stream(myClass.getClass().getDeclaredMethods())
                    .filter(clazzMethod -> clazzMethod.isAnnotationPresent(Log.class))
                    .collect(Collectors.toList());

            Optional<Method> optionalMethod = logMethods.stream()
                    .filter(clazzMethod -> clazzMethod.getName().equals(method.getName()) &&
                            Arrays.equals(clazzMethod.getParameterTypes(), method.getParameterTypes()))
                    .findFirst();

            if(optionalMethod.isPresent()) System.out.println("executed method: " + method.getName() + ", param: " + Arrays.toString(args));
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
