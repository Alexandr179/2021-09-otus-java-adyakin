package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.crm.model.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private static final Logger log = LoggerFactory.getLogger(DataTemplateJdbc.class);

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData<T> entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;


    public DataTemplateJdbc(DbExecutor dbExecutor,
                            EntitySQLMetaData<T> entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }


    @Override
    public Optional<T> findById(Connection connection, long id) {
        try {
            var modelObject = dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(),
                    List.of(id), rs -> {
                        try {
                            if (rs.next()) {
                                List<Object> objectParamsByRs = getObjectParamsByRs(rs);
                                String values = objectParamsByRs.stream().map(Object::toString).collect(Collectors.joining(", "));
                                T newInstance = entityClassMetaData.getConstructor().newInstance(values);// todo: почему не то кол-во аргумантов??
                                                                                                    // todo:  ..конструктор клиента (проверено), сущность тоже в норме с двумя полями..

                                return entityClassMetaData.getConstructor().newInstance(objectParamsByRs);
                            }
                        } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                            log.error(e.getMessage(), e);
                            throw new DataTemplateException(e);
                        }
                        return null;
                    });
            return Optional.of((T) modelObject);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }


    @Override
    public List<T> findAll(Connection connection) {
        var objectsList = new ArrayList<T>();
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            try {
                while (rs.next()) {
                    List<Object> objectParamsByRs = getObjectParamsByRs(rs);
                    try {
                        objectsList.add(entityClassMetaData.getConstructor().newInstance(objectParamsByRs));// set all params at <T>Object and add to List
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                return objectsList;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }


    @Override
    public long insert(Connection connection, T modelObject) {
        List<Object> params = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(modelObject);
                params.add(fieldValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T modelObject) {
        insert(connection, modelObject);
    }



    private List<Object> getObjectParamsByRs(ResultSet rs) {
        List<Object> collectObjects = new ArrayList<>();
        for (Field field : entityClassMetaData.getAllFields()){
            try {
                collectObjects.add(rs.getObject(field.getName()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return collectObjects;
    }
}
