package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataTemplateJdbc<T> implements DataTemplate<T> {

//    private static final Logger log = LoggerFactory.getLogger(ExecutorDemo.class);

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
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(id), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return entityClassMetaData.getConstructor().newInstance(getObjectParamsByRs(rs));
                }
            } catch (SQLException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
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

    private List<Object> getObjectParamsByRs(ResultSet rs) throws SQLException {
        List<Object> collectObjects = new ArrayList<>();
        collectObjects.add(entityClassMetaData.getIdField());// get id_value
        collectObjects.add(entityClassMetaData.getFieldsWithoutId()// get others_fields_values
                .stream()
                .<ResultSet>map(field -> {
                    try {
                        rs.getObject(field.getName());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList()));
        return collectObjects;
    }

    @Override
    public long insert(Connection connection, T modelObject) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(modelObject),
                    Collections.singletonList(entityClassMetaData.getConstructor().newInstance(modelObject)));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T modelObject) {
        insert(connection, modelObject);
    }
}
