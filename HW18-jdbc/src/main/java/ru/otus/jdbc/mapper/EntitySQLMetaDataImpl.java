package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;


public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData<T> {

    private final EntityClassMetaData<T> entityClassMetaData;


    public EntitySQLMetaDataImpl(EntityClassMetaDataImpl<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }


    @Override
    public String getSelectByIdSql() {
        return "select * from " + entityClassMetaData.getName().toLowerCase() + " where " +
                entityClassMetaData.getIdField().getName().toLowerCase() + " = ?";
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + entityClassMetaData.getName().toLowerCase();
    }

    @Override
    public String getInsertSql() {
        String fields = entityClassMetaData.getFieldsWithoutId().stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
        String values = entityClassMetaData.getFieldsWithoutId().stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));
        return "insert into " + entityClassMetaData.getName().toLowerCase() +
                " (" + fields + ")" +
                " values(" + values + ")";
    }

    @Override
    public String getUpdateSql() {
        return getInsertSql();
    }
}
