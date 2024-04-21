package com.boilerplate.utils.datagrid.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GridModel {
    private Integer pageNumber = 0;
    private Integer pageSize = 10;
    private String filter = "";
    private String sortField;
    private Integer sortDir;
    public Map<String, Object> getFilterList(){
        Map<String, Object> result = new HashMap<>();
        if(Objects.nonNull(filter) && !filter.isBlank()){
            String[] filters = filter.split("AND");
            List.of(filters).forEach(
                    f -> {
                        String[] keyValue = f.split("=");
                        result.put(keyValue[0], keyValue[1]);
                    }
            );
        }
        return result;
    }
}
