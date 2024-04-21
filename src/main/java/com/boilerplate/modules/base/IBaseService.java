package com.boilerplate.modules.base;


import com.boilerplate.utils.datagrid.domain.GridModel;
import com.boilerplate.configuration.exceptions.DataNotFoundException;
import com.boilerplate.configuration.exceptions.DuplicatedException;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBaseService<Type, TypeDto, ID> {
    Type save(Type entity);
    TypeDto create(TypeDto dto) throws DuplicatedException;
    TypeDto update(ID id, TypeDto dto) throws DataNotFoundException, DuplicatedException;
    List<TypeDto> getAll();
    TypeDto getById(ID id) throws DataNotFoundException;
    Page<TypeDto> list(GridModel gridModel, BooleanBuilder where);
    void remove(ID id) throws DataNotFoundException;
}
