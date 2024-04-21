package com.boilerplate.modules.base;

import com.boilerplate.configuration.exceptions.DataNotFoundException;
import com.boilerplate.configuration.exceptions.DuplicatedException;
import com.boilerplate.utils.datagrid.domain.GridModel;
import com.boilerplate.utils.datagrid.helpers.SortHelper;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseService<Type, TypeDto, ID> implements IBaseService<Type, TypeDto, ID> {

    public abstract BaseRepository<Type, ID> getRepository();
    public abstract BaseMapper<Type, TypeDto> getMapper();
    public abstract String getEntityName();
    @Override
    public Type save(Type entity) {
        return getRepository().saveAndFlush(entity);
    }

    @Override
    public TypeDto create(TypeDto typeDto) throws DuplicatedException {
        return getMapper().toDto(
                save(getMapper().toEntity(typeDto))
        );
    }

    @Override
    public TypeDto update(ID id, TypeDto typeDto) throws DataNotFoundException {
        Optional<Type> optional = getRepository().findById(id);
        if (optional.isEmpty()) {
            throw new DataNotFoundException(getEntityName() + " not found");
        }
        Type existing = optional.get();
        getMapper().partialUpdate(existing, typeDto);
        existing = save(existing);
        return getMapper().toDto(existing);
    }

    @Override
    public List<TypeDto> getAll() {
        return getRepository().findAll().stream()
                .map(getMapper()::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TypeDto getById(ID id) throws DataNotFoundException {
        Optional<Type> optional = getRepository().findById(id);
        if (optional.isEmpty()) {
            throw new DataNotFoundException(getEntityName() + " not found");
        }
        return getMapper().toDto(optional.get());
    }

    @Override
    public Page<TypeDto> list(GridModel params, BooleanBuilder where) {
        Pageable pageable = PageRequest.of(
                params.getPageNumber(),
                params.getPageSize(),
                SortHelper.sortByAndDesc(params.getSortDir(), params.getSortField())
        );
        return getRepository().findAll(where.getValue(), pageable).map(getMapper()::toDto);
    }

    @Override
    public void remove(ID id) throws DataNotFoundException {
        Optional<Type> optional = getRepository().findById(id);
        if (optional.isEmpty()) {
            throw new DataNotFoundException(getEntityName() + " not found");
        }
        getRepository().deleteById(id);
    }
}
