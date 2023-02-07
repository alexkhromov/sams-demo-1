package com.github.alexkhromov.model.mapper.impl;

import com.github.alexkhromov.model.dto.TitleDTO;
import com.github.alexkhromov.model.entity.Title;
import com.github.alexkhromov.model.mapper.IDTOMapper;
import org.springframework.stereotype.Component;

import static com.github.alexkhromov.model.enums.LocaleEnum.fromCode;

@Component
public class TitleDTOMapper implements IDTOMapper<TitleDTO, Title> {

    @Override
    public TitleDTO mapToDTO(Title entity) {

        TitleDTO dto = new TitleDTO();

        dto.setTitle(entity.getTitle());
        dto.setLocale(fromCode(entity.getLocale().getCode()));

        return dto;
    }

    @Override
    public Title mapToEntity(TitleDTO entity) {
        return null;
    }
}