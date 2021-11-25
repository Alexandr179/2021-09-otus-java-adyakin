package ru.otus.reflection;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class Reflection {

    public static void reflectionLogProcess(Method interfaceMethod, Object[] args) throws IOException, ClassNotFoundException {
        Class<?> interfaceClazz = interfaceMethod.getDeclaringClass();
        Package aPackage = interfaceClazz.getPackage();

        String name = interfaceMethod.getName();
        Class<?>[] clazzes = ClassLoaderHelper.getClasses(aPackage.getName());
        Optional<Class<?>> optClazzWithLogAnnotationMethod = Arrays.stream(clazzes)
                .filter(clazz -> {
                    Optional<Method> implClassWithLogAnnotationMethod = Arrays.stream(clazz.getDeclaredMethods())
                            .filter(classMethod -> classMethod.getName().equals(name) && classMethod.isAnnotationPresent(Log.class))
                            .findFirst();
                    return implClassWithLogAnnotationMethod.isPresent();
                })
                .findFirst();

        if(optClazzWithLogAnnotationMethod.isPresent())
            System.out.println("executed method: " + interfaceMethod.getName() + ", param: " + Arrays.toString(args));
    }
}