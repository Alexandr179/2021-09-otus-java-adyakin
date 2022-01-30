package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.config.AppConfig;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final Map<String, Object> appComponentsByClass = new HashMap<>();


    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }


    private void processConfig(Class<?> configClass) throws IllegalAccessException, InstantiationException {
        checkConfigClass(configClass);
        List<Method> orderedMethods = stream(configClass.getDeclaredMethods())
                .sorted(Comparator.comparingInt(method -> method.getDeclaredAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());
        for (Method methodConfigClass : orderedMethods) {
            Object been;
            List<Object> args = beenHasArgs(methodConfigClass);
            if (!args.isEmpty()) {
                been = ReflectionHelper.callMethod(configClass.newInstance(), methodConfigClass.getName(), args.toArray());
            } else {
                been = ReflectionHelper.callMethod(configClass.newInstance(), methodConfigClass.getName());
            }
            String beenName = methodConfigClass.getReturnType().getSimpleName();
            appComponentsByClass.put(beenName, been);
        }
    }


    private List<Object> beenHasArgs(Method methodConfigClass) {
        List<Object> args = new ArrayList<>();
        for (Parameter parameter : methodConfigClass.getParameters()) {
            String beenName = parameter.getType().getSimpleName();
            args.add(appComponentsByClass.get(beenName));
        }
        return args;
    }


    private Class<?> getClazz(Class<?> interfaceClass) throws ClassNotFoundException, IOException {
        Class<?>[] packageClazzes = ClassLoaderHelper.getClasses(interfaceClass.getPackage().getName());
        long mappingClazzCount = 0;
        Class<?> returnedClazz = null;
        for (Class<?> clazz : packageClazzes) {
            if (clazz.isAnnotationPresent(AppComponent.class)) {
                for (Method classMethod : clazz.getDeclaredMethods()) {
                    String classMethodName = classMethod.getName();
                    mappingClazzCount += stream(interfaceClass.getDeclaredMethods())
                            .filter(method -> method.getName().equals(classMethodName) &&
                                    method.getReturnType().equals(classMethod.getReturnType()))
                            .count();
                }
                if (interfaceClass.getDeclaredMethods().length == mappingClazzCount) {
                    returnedClazz = clazz;
                    break;
                }
            }
        }
        return returnedClazz;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        if(componentClass.isInterface())
            return (C) appComponentsByClass.get(componentClass.getSimpleName());
        Class<?>[] interfaces = componentClass.getInterfaces();
        componentClass = (Class<C>) interfaces[0];// provided is only one interface realisation
        return getAppComponent(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Optional<Class<?>> optionalClass = stream(AppConfig.class.getDeclaredMethods())
                .filter(method -> method.getName().equals(componentName))
                .findFirst()
                .map(Method::getReturnType);
        return (C) getAppComponent(optionalClass.get());
    }
}
