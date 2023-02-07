package com.github.alexkhromov.service;

import com.github.alexkhromov.model.dto.CreateQuestionDTO;
import com.github.alexkhromov.model.dto.ReadAllQuestionDTO;
import com.github.alexkhromov.model.dto.UpdateQuestionDTO;
import com.github.alexkhromov.model.entity.Question;
import com.github.alexkhromov.model.error.exception.SamsDemoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IQuestionService {

    Page<ReadAllQuestionDTO> findAll(String level, String locale, Pageable pageable) throws SamsDemoException;

    Page<ReadAllQuestionDTO> findAllForTranslation(String locale, Pageable pageable) throws SamsDemoException;

    Page<ReadAllQuestionDTO> findByQuery(String query, String locale, Pageable pageable) throws SamsDemoException;

    Question findById(Long questionId) throws SamsDemoException;

    Question findByIdAndByPassProxy(Long questionId) throws SamsDemoException;

    Question save(CreateQuestionDTO questionDTO) throws SamsDemoException;

    Question update(Long questionId, UpdateQuestionDTO questionDTO) throws SamsDemoException;

    void delete(Long questionId) throws SamsDemoException;
}