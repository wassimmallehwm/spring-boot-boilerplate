package com.boilerplate.utils.datagrid.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GridPaginationParams {
    @JsonProperty("pageNumber")
    private int pageNumber;
    @JsonProperty("pageSize")
    private int pageSize;
    @JsonProperty("filterModel")
    private List<GridFilterModel> filterModel;
    @JsonProperty("sortModel")
    private List<GridSortModel> sortModel;
}
