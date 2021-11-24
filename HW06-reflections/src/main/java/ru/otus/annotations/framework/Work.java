package ru.otus.annotations.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.otus.annotations.framework.ReflectionHelper.callMethod;

public class Work {

    public static String clazzName = "ru.otus.annotations.framework.TestedClass";

    public static void main(String[] args) {
        try {
            work(clazzName);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    private static void work(String clazzName) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> clazz = Class.forName(clazzName);
        Constructor<?> constructor = clazz.getConstructor();

        AtomicInteger successCounter = new AtomicInteger();
        AtomicInteger failedCounter = new AtomicInteger();

        List<String> beforeMethods = new ArrayList<>();
        List<String> afterMethods = new ArrayList<>();
        List<String> testMethods = new ArrayList<>();

        Method[] methodsAll = clazz.getDeclaredMethods();
        annotationsToListsScan(methodsAll, beforeMethods, afterMethods, testMethods);

        testMethods.forEach(testMethodName -> {
                    try {
                        Object eachTestsObject = constructor.newInstance();
                        beforeMethods.forEach(beforeMethodName -> callMethod(eachTestsObject, beforeMethodName));
                        callMethod(eachTestsObject, testMethodName);
                        afterMethods.forEach(afterMethodName -> callMethod(eachTestsObject, afterMethodName));
                        successCounter.getAndIncrement();
                    } catch (Exception e) {// any sort of Exception from tested methods..
                        failedCounter.getAndIncrement();
                    }
                }
        );

        System.out.println("STATISTIC. Tests: " + (successCounter.get() + failedCounter.get()) +
                " methods. Successes: " + successCounter.get() +
                ". Failed: " + failedCounter.get());
    }


    private static void annotationsToListsScan(Method[] methodsAll, List<String> beforeOnMethod, List<String> afterOnMethod, List<String> testOnMethod) {
        Arrays.stream(methodsAll).forEach(method -> {
            Annotation[] annotations = method.getDeclaredAnnotations();
            Arrays.stream(annotations)
                    .forEach(annotationOnMethod -> {
                        Class<? extends Annotation> aClass = annotationOnMethod.annotationType();
                        if (Before.class.equals(aClass)) {
                            beforeOnMethod.add(method.getName());
                        } else if (Test.class.equals(aClass)) {
                            testOnMethod.add(method.getName());
                        } else if (After.class.equals(aClass)) {
                            afterOnMethod.add(method.getName());
                        }
                    });
        });
    }
}
