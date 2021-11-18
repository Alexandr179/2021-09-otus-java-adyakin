package ru.otus.reflection;

import org.reflections.Reflections;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class Reflection {

    public static void reflectionLogProcess(Object object, Method interfaceMethod, Object[] args) throws NoSuchMethodException {
        Class<?> interfaceClazz = interfaceMethod.getDeclaringClass();
        Set<Class<?>> clazzesSet = new Reflections("ru.otus").getSubTypesOf((Class<Object>) interfaceClazz);// https://qna.habr.com/q/51694

        for (Class<?> clazz : clazzesSet) {
            Method clazzMethod = clazz.getDeclaredMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
            Annotation[] annotations = clazzMethod.getDeclaredAnnotations();
            Arrays.stream(annotations)
                    .findAny()
                    .ifPresent(annotation -> {
                        try {
                            if (annotation.annotationType().getTypeName().contains("Log")) {
                                System.out.println("executed method: " + clazzMethod.getName() + ", param: " + Arrays.toString(args));
                            }
                        } catch (Exception ignored) {
                        }
                    });
        }
    }
}