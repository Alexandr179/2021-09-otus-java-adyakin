package ru.otus.annotations.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.otus.annotations.framework.ReflectionHelper.callMethod;

public class Work {

    public static String clazzName = "ru.otus.annotations.framework.TestedClass";

    public static void main(String[] args) {
        try {
            try {
                work(clazzName);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private static void work(String clazzName) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName(clazzName);
        Constructor<?> constructor = clazz.getConstructor();
        Object object = constructor.newInstance();//  "..СВОЙ объект класса-теста"
        Method[] methodsAll = clazz.getDeclaredMethods();

        AtomicInteger successCounter = new AtomicInteger();
        AtomicInteger failedCounter = new AtomicInteger();
        Arrays.stream(methodsAll).forEach(method -> {
            Annotation[] annotations = method.getDeclaredAnnotations();
            Arrays.stream(annotations)
                    .findAny()
                    .ifPresent(annotation -> {
                        if (annotation.annotationType().equals(Before.class)) {
                            try {
                                callMethod(object, method.getName());
                                successCounter.getAndIncrement();
                            } catch (Throwable e) {
                                failedCounter.getAndIncrement();
                            }
                        } else if (annotation.annotationType().equals(Test.class)) {
                            try {
                                callMethod(object, method.getName());
                                successCounter.getAndIncrement();
                            } catch (Throwable e) {
                                failedCounter.getAndIncrement();
                            }
                        } else if (annotation.annotationType().equals(After.class)) {
                            try {
                                callMethod(object, method.getName());
                                successCounter.getAndIncrement();
                            } catch (Throwable e) {
                                failedCounter.getAndIncrement();
                            }
                        }
                    });
        });

        int success = successCounter.get();
        int fail = failedCounter.get();
        int total = success + fail;
        System.out.println("Statistic: tests " + total + " methods. Successes - " + success + ", failed - " + fail);
    }
}
