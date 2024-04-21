package com.boilerplate.utils.datagrid.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GridFilterModel {
    private Long id;
    private String columnField;
    private String operatorValue;
    private String value;
}
