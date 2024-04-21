package com.boilerplate.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppPage<T> {

    private List<T> content;
    private Integer size;
    private Integer numberOfElements;
    private Integer totalElements;
    private Integer totalPages;

}
