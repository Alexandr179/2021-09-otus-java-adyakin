package ru.otus.reflection;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class Reflection {

    public static boolean annotationOnMethodPresent;

    public static void reflectionLogProcess(Method interfaceMethod, Object[] args) {
        Class<?> interfaceClazz = interfaceMethod.getDeclaringClass();
        Package aPackage = interfaceClazz.getPackage();
        Set<Class<?>> classesSet = new Reflections(aPackage.getName()).getSubTypesOf((Class<Object>) interfaceClazz);// https://qna.habr.com/q/51694
        classesSet.forEach(clazz -> {
            try {
                Method declaredMethod = clazz.getDeclaredMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
                annotationOnMethodPresent = declaredMethod.isAnnotationPresent(Log.class);
                if(annotationOnMethodPresent)  System.out.println("executed method: " + declaredMethod.getName() + ", param: " + Arrays.toString(args));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        });
    }
}