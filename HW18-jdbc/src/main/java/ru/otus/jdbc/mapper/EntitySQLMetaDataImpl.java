package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData<T> {

    private EntityClassMetaData<T> entityClassMetaData;


    public EntitySQLMetaDataImpl() {
        this.entityClassMetaData = new EntityClassMetaDataImpl<>();
    }


    @Override
    public String getSelectByIdSql(Long id) {
        return "select * from " + entityClassMetaData.getName().toLowerCase() + " where " +
                entityClassMetaData.getIdField().toString().toLowerCase() + " = " + id;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + entityClassMetaData.getName().toLowerCase();
    }

    @Override
    public String getInsertSql(T modelObject) {
        entityClassMetaData.setModelObjectType((Class<T>)modelObject.getClass());

        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        List<Object> valuesByModelObjects = fieldsWithoutId.stream().map(field -> {
            try {
                return modelObject.getClass().getDeclaredField(field.getName()).get(modelObject);
            } catch (IllegalAccessException | NoSuchFieldException ignored) {// casted ()
            }
            return null;
        }).collect(Collectors.toList());
        return "insert into " + entityClassMetaData.getName().toLowerCase() + "(" + fieldsWithoutId + ")" +
                " values (" + valuesByModelObjects + ")";
    }

    @Override
    public String getUpdateSql(T modelObject) {
        return getInsertSql(modelObject);
    }
}
