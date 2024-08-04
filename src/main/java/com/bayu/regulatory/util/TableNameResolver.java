package com.bayu.regulatory.util;

import jakarta.persistence.Table;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class TableNameResolver {

    public static String getTableName(Class<?> entityClass) {
        Optional<Table> tableAnnotation = Optional.ofNullable(entityClass.getAnnotation(Table.class));
        return tableAnnotation.map(Table::name).orElse(entityClass.getSimpleName());
    }

}
