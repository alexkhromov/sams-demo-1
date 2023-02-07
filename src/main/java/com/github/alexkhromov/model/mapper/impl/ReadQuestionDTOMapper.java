package com.github.alexkhromov.model.mapper.impl;

import com.github.alexkhromov.model.dto.ReadQuestionDTO;
import com.github.alexkhromov.model.dto.TitleDTO;
import com.github.alexkhromov.model.entity.Question;
import com.github.alexkhromov.model.entity.Title;
import com.github.alexkhromov.model.mapper.IDTOMapper;
import com.github.alexkhromov.security.SecurityPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.github.alexkhromov.common.ApplicationConstant.ANONYMOUS_USER_ID;

@Component
public class ReadQuestionDTOMapper implements IDTOMapper<ReadQuestionDTO, Question> {

    private IDTOMapper<TitleDTO, Title> titleDTOMapper;

    @Autowired
    public ReadQuestionDTOMapper(IDTOMapper<TitleDTO, Title> titleDTOMapper) {
        this.titleDTOMapper = titleDTOMapper;
    }

    @Override
    public ReadQuestionDTO mapToDTO(Question entity) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = ANONYMOUS_USER_ID;
        if (authentication != null &&
                authentication.getPrincipal() != null &&
                !(authentication instanceof AnonymousAuthenticationToken)) {

            userId = ((SecurityPrincipal)authentication.getPrincipal()).getUserId();
        }

        ReadQuestionDTO dto = new ReadQuestionDTO();

        dto.setId(entity.getId());
        dto.setLink(entity.getLink());
        dto.setLevel(entity.getLevel().getType());
        dto.setTitles(titleDTOMapper.mapToDTOList(entity.getTitles()));
        dto.setIsOwner(userId.equals(entity.getUser().getId()));

        return dto;
    }
}