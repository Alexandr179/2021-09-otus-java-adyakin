package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws ClassNotFoundException, IOException, IllegalAccessException, InstantiationException {
        checkConfigClass(configClass);

        List<Method> orderedMethods = stream(configClass.getDeclaredMethods())
                .sorted(Comparator.comparingInt(method -> method.getDeclaredAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());

        for (Method methodConfigClass : orderedMethods) {
            List<Object> args = new ArrayList<>();
            for(Parameter parameter : methodConfigClass.getParameters()){
                String beenName = parameter.getType().getSimpleName();
                Object been = null;
                if (appComponents.contains(beenName)) {
                    been = appComponentsByName.get(beenName);
                } else {
                    try {
                        Class<?> clazz = getClazz(parameter.getType());
                        been = ReflectionHelper.callMethod(clazz.newInstance(), methodConfigClass.getName());
                        appComponentsByName.put(methodConfigClass.getReturnType().getSimpleName(), been);
                        appComponents.add(methodConfigClass.getReturnType().getSimpleName());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                args.add(been);
            }

            Object been;
            if(!args.isEmpty()){
                been = ReflectionHelper.callMethod(configClass.newInstance(), methodConfigClass.getName(), args.toArray());
            } else {
                been = ReflectionHelper.callMethod(configClass.newInstance(), methodConfigClass.getName());
            }

            appComponentsByName.put(methodConfigClass.getReturnType().getSimpleName(), been);
            appComponents.add(methodConfigClass.getReturnType().getSimpleName());
        }
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
        return (C) appComponentsByName.get(componentClass.getSimpleName());
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
