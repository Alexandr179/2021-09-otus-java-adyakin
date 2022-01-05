package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplateException;
import ru.otus.crm.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * processing reflection only by Class<T>
 * @param <T>
 */
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    public Class<T> modelObjectType;

    public EntityClassMetaDataImpl(Class<T> modelObjectType) {
        this.modelObjectType = modelObjectType;
    }

    @Override
    public String getName() {
        return modelObjectType.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor(){
        try {
            return modelObjectType.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new DataTemplateException(new IllegalAccessException());
        }
    }

    @Override
    public Field getIdField() {
        return getAllFields().stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow(UnsupportedOperationException::new);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(modelObjectType.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream()
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }
}
