package com.boilerplate.modules.base;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Contract for a generic dto to entity mapper.
 *
 * @param <TypeDto> - DTO type parameter.
 * @param <Type> - Entity type parameter.
 */

public interface BaseMapper<Type, TypeDto> {

    Type toEntity(TypeDto dto);

    @BeanMapping(ignoreByDefault = false)
    TypeDto toDto(Type entity);

    /*List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);*/

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Type entity, TypeDto dto);
}
