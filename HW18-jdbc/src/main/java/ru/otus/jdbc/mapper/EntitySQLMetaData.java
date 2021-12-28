package ru.otus.jdbc.mapper;

/**
 * Создает SQL - запросы
 */
public interface EntitySQLMetaData<T> {
    String getSelectAllSql();

    String getSelectByIdSql(Long id);

    String getInsertSql(T modelObject);

    String getUpdateSql(T modelObject);
}
