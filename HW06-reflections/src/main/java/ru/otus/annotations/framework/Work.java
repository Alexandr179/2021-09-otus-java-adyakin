package ru.otus.annotations.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

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


    /**
     *   assertThat().isEqualTo()  Framework primitive realisation
     */
    private static void work(String clazzName) throws IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName(clazzName);
        Constructor<?> constructor = clazz.getConstructor();

        Method[] methodsAll = clazz.getDeclaredMethods();
        Field[] fieldsPublic = clazz.getFields();
        Optional<Field> fieldOptional = Arrays.stream(fieldsPublic).findFirst();

        Object object = constructor.newInstance();
        Field field = fieldOptional.get();
        Integer account = (Integer) field.get(object);

        Arrays.stream(methodsAll).forEach(method -> {
//            System.out.println("Method is: " + method.getName() + "()");
            Annotation[] annotations = method.getDeclaredAnnotations();
//            System.out.println(Arrays.toString(annotations));
            Arrays.stream(annotations)
                    .findAny()
                    .ifPresent(annotation -> {
                        if(annotation.annotationType().getTypeName().contains("Before")) {
                            ReflectionHelper.callMethod(object, "increment");
                            Integer incremented = (Integer) ReflectionHelper.getFieldValue(object, "account");
                            if (account == incremented - 1) {
                                System.out.println("Success test @Before: " + method.getName() + "()");
                            } else {
                                System.err.println("Failed test @Before: " + method.getName() + "()");
                            }
                        }
                        else if(annotation.annotationType().getTypeName().contains("Test")) {
                            ReflectionHelper.callMethod(object, "tested");
                            Integer incremented = (Integer) ReflectionHelper.getFieldValue(object, "account");
                            if (Objects.equals(account, incremented - 1)) {
                                System.out.println("Success test @Test: " + method.getName() + "()");
                            } else {
                                System.err.println("Failed test @Test: " + method.getName() + "()");
                            }
                        }
                        else if(annotation.annotationType().getTypeName().contains("After")) {
                            ReflectionHelper.callMethod(object, "goBack");
                            Integer incremented = (Integer) ReflectionHelper.getFieldValue(object, "account");
                            if (account == incremented) {
                                System.out.println("Success test @After: " + method.getName() + "()");
                            } else {
                                System.err.println("Failed test @After: " + method.getName() + "()");
                            }
                        }
                    });
        });
    }
}
