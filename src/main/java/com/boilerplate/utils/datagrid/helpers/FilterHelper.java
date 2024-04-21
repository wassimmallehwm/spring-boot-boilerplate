package com.boilerplate.utils.datagrid.helpers;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;

import java.util.Map;
import java.util.Set;

public class FilterHelper {

    public static <T> void filter(String name, Map<String, Object> filters, BooleanBuilder where, Class<? extends T> type){
        PathBuilder<T> en = new PathBuilder<T>(type, name);
        Predicate filter;
        for (var entry : filters.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            if(fieldName.contains("displayName")){
                filter = en.getString(
                                fieldName.replace("displayName", "firstname"))
                        .concat(fieldName.replace("displayName","lastname"))
                        .containsIgnoreCase(fieldValue.toString().replace(" ", ""));
                where.and(filter);
                return;
            }
            filter = en.getString(fieldName).equalsIgnoreCase(fieldValue.toString());
            where.and(filter);
            System.out.println(fieldName + "/" + entry.getValue());
        }
    }
}
