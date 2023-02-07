package com.github.alexkhromov.service.impl;

import com.github.alexkhromov.common.ApplicationConstant;
import com.github.alexkhromov.model.dto.CreateQuestionDTO;
import com.github.alexkhromov.model.dto.ReadAllQuestionDTO;
import com.github.alexkhromov.model.dto.TitleDTO;
import com.github.alexkhromov.model.dto.UpdateQuestionDTO;
import com.github.alexkhromov.model.entity.*;
import com.github.alexkhromov.model.error.exception.SamsDemoException;
import com.github.alexkhromov.repository.LevelConRepository;
import com.github.alexkhromov.repository.QuestionRepository;
import com.github.alexkhromov.repository.UserRepository;
import com.github.alexkhromov.security.SecurityPrincipal;
import com.github.alexkhromov.service.IQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.github.alexkhromov.model.error.ErrorCode.*;
import static com.github.alexkhromov.model.error.exception.SamsDemoException.*;
import static com.github.alexkhromov.security.SecurityExpression.*;
import static java.util.Collections.singletonList;

@Slf4j
@Service
public class QuestionService implements IQuestionService {

    private final QuestionRepository questionRepository;
    private final LevelConRepository levelConRepository;
    private final UserRepository userRepository;

    @Autowired
    public QuestionService(
            QuestionRepository questionRepository,
            LevelConRepository levelConRepository,
            UserRepository userRepository) {

        this.questionRepository = questionRepository;
        this.levelConRepository = levelConRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReadAllQuestionDTO> findAll(
            String level, String locale, Pageable pageable) throws SamsDemoException {

        log.debug("Entered [findAll] with level = {}, locale = {}, pageable = {}", level, locale, pageable);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = ApplicationConstant.ANONYMOUS_USER_ID;
        if (authentication != null &&
                authentication.getPrincipal() != null &&
                !(authentication instanceof AnonymousAuthenticationToken)) {

            userId = ((SecurityPrincipal)authentication.getPrincipal()).getUserId();
        }

        try {
            return questionRepository.findAll(level, locale, userId, pageable);
        } catch (Exception ex) {

            log.error("Internal server exception [findAll]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('TRANSLATOR')")
    public Page<ReadAllQuestionDTO> findAllForTranslation(
            String locale, Pageable pageable) throws SamsDemoException {

        log.debug("Entered [findAllForTranslation] with pageable = {}", pageable);

        try {
            return questionRepository.findAllForTranslation(locale,pageable);
        } catch (Exception ex) {

            log.error("Internal server exception [findAllForTranslation]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReadAllQuestionDTO> findByQuery(
            String query, String locale, Pageable pageable) throws SamsDemoException {

        log.debug("Entered [findByQuery] with query = {}, locale = {}, pageable = {}", query, locale, pageable);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = ApplicationConstant.ANONYMOUS_USER_ID;
        if (authentication != null &&
                authentication.getPrincipal() != null &&
                !(authentication instanceof AnonymousAuthenticationToken)) {

            userId = ((SecurityPrincipal)authentication.getPrincipal()).getUserId();
        }

        try {
            return questionRepository.findByQuery(query, locale, userId, pageable);
        } catch (Exception ex) {

            log.error("Internal server exception [findByQuery]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize(READ_QUESTION_ACL)
    public Question findById(Long questionId) throws SamsDemoException {

        log.debug("Entered [findById] with questionId = {}", questionId);

        if (questionId == null) {

            log.error("Bad request exception [findById]: questionId is missing");
            throw badRequestException(ID_MISSING);
        }

        Optional<Question> optionalQuestion;
        try {
            optionalQuestion = questionRepository.findById(questionId);
        } catch (Exception ex) {

            log.error("Internal server exception [findById]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }

        if (!optionalQuestion.isPresent()) {

            log.error("Entity not found exception [findById]: {}, {}",
                    Question.class.getSimpleName(), questionId.toString());

            throw entityNotFoundException(
                    ENTITY_NOT_FOUND,
                    Question.class.getSimpleName(),
                    questionId.toString());
        }

        log.debug("Exited [findById] with question = {}", optionalQuestion.get());

        return optionalQuestion.get();
    }

    @Override
    public Question findByIdAndByPassProxy(Long questionId) throws SamsDemoException {

        log.debug("Entered [findByIdAndByPassProxy] with questionId = {}", questionId);

        return this.findById(questionId);
    }

    @Override
    @Transactional
    @PreAuthorize(CREATE_QUESTION_ACL)
    public Question save(CreateQuestionDTO questionDTO) throws SamsDemoException {

        log.debug("Entered [save] with questionDTO = {}", questionDTO);

        LevelCon level;
        try {
            level = levelConRepository.findByType(questionDTO.getLevel());
        } catch (Exception ex) {

            log.error("Internal server exception [save]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }

        Title title = new Title();
        title.setTitle(questionDTO.getTitle());
        title.setLocale(findRequiredLocale(level, questionDTO.getLocale().getValue()));
        title.setId(new TitleId());
        title.getId().setLocaleId(title.getLocale().getId());

        Question question = new Question();
        question.setLevel(level);
        question.setLink(questionDTO.getLink());
        question.setTitles(singletonList(title));
        question.setIsFullyLocalized(false);

        title.setQuestion(question);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {

            log.error("Internal server exception [save]: " +
                    "unexpected error - securityPrincipal is null but method is secured");

            throw internalServerException(UNEXPECTED_ERROR);
        }

        SecurityPrincipal  securityPrincipal =
                (SecurityPrincipal) authentication.getPrincipal();

        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(securityPrincipal.getUserId());
        } catch (Exception ex) {

            log.error("Internal server exception [save]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }

        if (!optionalUser.isPresent()) {

            log.error("Internal server exception [save]: " +
                    "unexpected error - user is null but method is secured");

            throw internalServerException(UNEXPECTED_ERROR);
        }

        question.setUser(optionalUser.get());

        try {
            return questionRepository.save(question);
        } catch (Exception ex) {

            log.error("Internal server exception [save]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }
    }

    @Override
    @Transactional
    @PreAuthorize(UPDATE_QUESTION_ACL)
    public Question update(Long questionId, UpdateQuestionDTO questionDTO) throws SamsDemoException {

        log.debug("Entered [update] with questionId = {}, questionDTO = {}", questionId, questionDTO);

        Question question = findById(questionId);

        LevelCon level;
        try {
            level = levelConRepository.findByType(questionDTO.getLevel());
        } catch (Exception ex) {

            log.error("Internal server exception [update]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }

        questionDTO
                .getTitles()
                .forEach(titleDTO -> updateTitle(question, titleDTO));

        question.setLink(questionDTO.getLink());
        question.setLevel(level);
        question.setIsFullyLocalized(question.getTitles().size() == ApplicationConstant.SUPPORTED_LOCALES);

        try {
            return questionRepository.save(question);
        } catch (Exception ex) {

            log.error("Internal server exception [update]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }
    }

    @Override
    @PreAuthorize(DELETE_QUESTION_ACL)
    public void delete(Long questionId) throws SamsDemoException {

        log.debug("Entered [delete]");

        try {
            questionRepository.deleteById(questionId);
        } catch (EmptyResultDataAccessException ex) {

            log.error("Entity not found exception [delete]: {}, {}",
                    Question.class.getSimpleName(), questionId.toString());

            throw entityNotFoundException(
                    ENTITY_NOT_FOUND,
                    Question.class.getSimpleName(),
                    questionId.toString());
        }
    }

    private Locale findRequiredLocale(LevelCon level, String locale) throws SamsDemoException {

        Optional<Locale> optionalLocale = level
                .getLocalizedLevels()
                .stream()
                .filter(ll -> ll.getLocale().getCode().equals(locale))
                .map(LevelLocalized::getLocale)
                .findAny();

        if (optionalLocale.isPresent()) {
            return optionalLocale.get();
        } else {

            log.error("Bad request exception [findRequiredLocale]: locale not supported exception: {}", locale);
            throw badRequestException(LOCALE_NOT_SUPPORTED, locale);
        }
    }

    private void updateTitle(Question question, TitleDTO titleDTO) {

        Optional<Title> existingTitle = question.getTitles()
                .stream()
                .filter(title -> title.getLocale().getCode().equals(titleDTO.getLocale().getValue()))
                .findFirst();

        if (existingTitle.isPresent()) {

            Title title = existingTitle.get();
            title.setTitle(titleDTO.getTitle());

        } else {

            Title title = new Title();
            title.setTitle(titleDTO.getTitle());
            title.setLocale(findRequiredLocale(question.getLevel(), titleDTO.getLocale().getValue()));
            title.setId(new TitleId());
            title.getId().setLocaleId(title.getLocale().getId());
            title.getId().setQuestionId(question.getId());
            question.getTitles().add(title);
            title.setQuestion(question);
        }
    }
}