package com.github.alexkhromov.model.mapper.impl;

import com.github.alexkhromov.common.ApplicationConstant;
import com.github.alexkhromov.model.dto.ReadUserDTO;
import com.github.alexkhromov.model.entity.Question;
import com.github.alexkhromov.model.entity.User;
import com.github.alexkhromov.model.mapper.IDTOMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class ReadUserDTOMapper implements IDTOMapper<ReadUserDTO, User> {

    @Override
    public ReadUserDTO mapToDTO(User entity) {

        ReadUserDTO dto = new ReadUserDTO();

        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setUsername(entity.getUsername());
        dto.setIsDeleted(entity.getIsDeleted());

        List<String> roles = entity.getRoles()
                .stream()
                .map(userRole -> userRole.getRoleCon().getRole().name())
                .collect(toList());

        dto.setRoles(roles);

        List<String> questionLinks = entity.getQuestions()
                .stream()
                .map(this::toLink)
                .collect(toList());

        dto.setQuestionLinks(questionLinks);

        return dto;
    }

    private String toLink(Question entity) {

        return ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path(ApplicationConstant.QUESTION_ENTITY_LOCATION)
                .build()
                .expand(entity.getId())
                .toUri()
                .toString();
    }
}