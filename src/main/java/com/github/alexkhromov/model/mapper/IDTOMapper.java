package com.github.alexkhromov.model.mapper;

import com.github.alexkhromov.model.dto.BaseDTO;
import com.github.alexkhromov.model.entity.BaseEntity;

import java.util.List;

import static java.util.stream.Collectors.toList;

public interface IDTOMapper<DTO extends BaseDTO, ENTITY extends BaseEntity> {

    default DTO mapToDTO(ENTITY entity) {
        return null;
    }

    default List<DTO> mapToDTOList(List<ENTITY> entityList) {

        return entityList
                .stream()
                .map(this::mapToDTO)
                .collect(toList());
    }

    default ENTITY mapToEntity(DTO entity) {
        return null;
    }
}