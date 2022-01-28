package ru.otus.appcontainer.api;

import java.lang.reflect.InvocationTargetException;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass) throws IllegalAccessException, InstantiationException, InvocationTargetException;
    <C> C getAppComponent(String componentName);
}
