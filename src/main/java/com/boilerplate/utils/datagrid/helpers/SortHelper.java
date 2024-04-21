package com.boilerplate.utils.datagrid.helpers;

import org.springframework.data.domain.Sort;

import java.util.Objects;

public class SortHelper {

    public static Sort sortByAndDesc(Integer sortDir, String sortField){
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        if(nullOrEmpty(sortField)){
            return sortById;
        }
        Sort.Direction dir = Objects.nonNull(sortDir) && sortDir == -1 ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, sortField)
                .and(sortById);
    }

    private static boolean nullOrEmpty(Object value){
        return Objects.isNull(value) || value.toString().isEmpty();
    }
}
