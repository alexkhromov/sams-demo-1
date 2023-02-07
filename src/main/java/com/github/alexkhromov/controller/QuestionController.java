package com.github.alexkhromov.controller;

import com.github.alexkhromov.common.ApplicationConstant;
import com.github.alexkhromov.service.IQuestionService;
import com.github.alexkhromov.model.dto.CreateQuestionDTO;
import com.github.alexkhromov.model.dto.ReadAllQuestionDTO;
import com.github.alexkhromov.model.dto.ReadQuestionDTO;
import com.github.alexkhromov.model.dto.UpdateQuestionDTO;
import com.github.alexkhromov.model.entity.Question;
import com.github.alexkhromov.model.error.exception.SamsDemoException;
import com.github.alexkhromov.model.mapper.IDTOMapper;
import com.github.alexkhromov.model.response.ResponseBuilder;
import com.github.alexkhromov.model.response.SamsDemoResponse;
import com.github.alexkhromov.web.annotation.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/questions")
public class QuestionController {

	private IQuestionService questionService;
	private IDTOMapper<ReadQuestionDTO, Question> readQuestionDTOMapper;

	@Autowired
	public QuestionController(IQuestionService questionService,
	                          IDTOMapper<ReadQuestionDTO, Question> readQuestionDTOMapper) {

		this.questionService = questionService;
		this.readQuestionDTOMapper = readQuestionDTOMapper;
	}

	@GetMapping
	public ResponseEntity<SamsDemoResponse<ReadAllQuestionDTO>> findAllQuestions(
			@Level String level,
			Locale locale,
			Pageable pageable) throws SamsDemoException {

		Page<ReadAllQuestionDTO> page = questionService.findAll(level, locale.toLanguageTag(), pageable);

		return ResponseBuilder
				.<ReadAllQuestionDTO, Question>success()
				.withPageData(page)
				.withHttpStatus(OK)
				.build();
	}

	@GetMapping("/translate")
	public ResponseEntity<SamsDemoResponse<ReadAllQuestionDTO>> findQuestionsForTranslation(
			Locale locale,
			Pageable pageable) throws SamsDemoException {

		Page<ReadAllQuestionDTO> page = questionService
				.findAllForTranslation(locale.toLanguageTag(), pageable);

		return ResponseBuilder
				.<ReadAllQuestionDTO, Question>success()
				.withPageData(page)
				.withHttpStatus(OK)
				.build();
	}
	@GetMapping("/query")
	public ResponseEntity<SamsDemoResponse<ReadAllQuestionDTO>> findQuestionsByQuery(
			@RequestParam(name = "query") String query,
			Locale locale,
			Pageable pageable) throws SamsDemoException {

		Page<ReadAllQuestionDTO> page = questionService
				.findByQuery(query, locale.toLanguageTag(), pageable);

		return ResponseBuilder
				.<ReadAllQuestionDTO, Question>success()
				.withPageData(page)
				.withHttpStatus(OK)
				.build();
	}

	@GetMapping("/{questionId}")
	public ResponseEntity<SamsDemoResponse<ReadQuestionDTO>> findQuestionById(
			@PathVariable(name = "questionId") Long questionId) throws SamsDemoException {

		Question question = questionService.findById(questionId);

		return ResponseBuilder
				.<ReadQuestionDTO, Question>success()
				.withData(singletonList(question), readQuestionDTOMapper)
				.withHttpStatus(OK)
				.build();
	}

	@PostMapping
	public ResponseEntity createQuestion(
			@RequestBody @Valid CreateQuestionDTO questionDTO) throws SamsDemoException {

		Question question = questionService.save(questionDTO);

		return ResponseBuilder
				.empty()
				.withLocation(ApplicationConstant.QUESTION_ENTITY_LOCATION, question.getId())
				.withHttpStatus(CREATED)
				.build();
	}

	@PutMapping("/{questionId}")
	public ResponseEntity<SamsDemoResponse<ReadQuestionDTO>> updateQuestion(
			@PathVariable(name = "questionId") Long questionId,
			@RequestBody @Valid UpdateQuestionDTO questionDTO) throws SamsDemoException {

		Question question = questionService.update(questionId, questionDTO);

		return ResponseBuilder
				.<ReadQuestionDTO, Question>success()
				.withData(singletonList(question), readQuestionDTOMapper)
				.withHttpStatus(OK)
				.build();
	}

	@DeleteMapping("/{questionId}")
	public ResponseEntity deleteQuestion(
			@PathVariable(name = "questionId") Long questionId) throws SamsDemoException{

		questionService.delete(questionId);

		return ResponseBuilder
				.empty()
				.withHttpStatus(NO_CONTENT)
				.build();
	}
}