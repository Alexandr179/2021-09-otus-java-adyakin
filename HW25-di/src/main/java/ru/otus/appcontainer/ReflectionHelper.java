package ru.otus.appcontainer;

import static java.util.Arrays.stream;


public class ReflectionHelper {
    private ReflectionHelper() {
    }

    public static Object getFieldValue(Object object, String name) {
        try {
            var field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void setFieldValue(Object object, String name, Object value) {
        try {
            var field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object callMethod(Object object, String name, Object... args) {
        try {
            Class<?> objectsClazz = object.getClass();
            Class<?> [] argsInterfaces = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                Class<?>[] interfaces = args[i].getClass().getInterfaces();
                argsInterfaces[i] = interfaces[0];// todo: provided is only one interface
            }
            var method = objectsClazz.getDeclaredMethod(name, argsInterfaces);
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object callMethod(Object object, String name) {
        try {
            var method = object.getClass().getDeclaredMethod(name);
            method.setAccessible(true);
            return method.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getDeclaredConstructor().newInstance();
            } else {
                Class<?>[] classes = toClasses(args);
                return type.getDeclaredConstructor(classes).newInstance(args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?>[] toClasses(Object[] args) {
        return stream(args).map(Object::getClass).toArray(Class<?>[]::new);
    }
}
