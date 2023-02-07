package com.github.alexkhromov.service.impl;

import com.github.alexkhromov.AbstractUnitTest;
import com.github.alexkhromov.TestBeanConfiguration;
import com.github.alexkhromov.common.ApplicationConstant;
import com.github.alexkhromov.model.enums.LevelType;
import com.github.alexkhromov.model.enums.LocaleEnum;
import com.github.alexkhromov.model.error.ErrorCode;
import com.github.alexkhromov.repository.LevelConRepository;
import com.github.alexkhromov.repository.QuestionRepository;
import com.github.alexkhromov.repository.UserRepository;
import com.github.alexkhromov.model.dto.CreateQuestionDTO;
import com.github.alexkhromov.model.dto.ReadAllQuestionDTO;
import com.github.alexkhromov.model.entity.Question;
import com.github.alexkhromov.model.entity.User;
import com.github.alexkhromov.model.error.exception.SamsDemoException;
import com.github.alexkhromov.security.SecurityPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.github.alexkhromov.model.enums.Role.TRANSLATOR;
import static com.github.alexkhromov.model.enums.Role.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.PageRequest.of;
import static org.springframework.http.HttpStatus.*;

class QuestionServiceTest {

    //ARRANGED DATA
    private static final int DEFAULT_TOTAL_PAGES = 6;
    private static final int QUESTIONS_TO_TRANSLATE = 0;

    private static final long QUESTIONS_NUMBER = 29;

    private static final long [] USER_QUESTION_IDS = {1, 5, 9, 13, 17, 21, 25, 29};
    private static final long [] ADMIN_QUESTION_IDS = {2, 6, 10, 14, 18, 22, 26};
    private static final long [] TRANSLATOR_QUESTION_IDS = {3, 7, 11, 15, 19, 23, 27};
    private static final long [] MODERATOR_QUESTION_IDS = {4, 8, 12, 16, 20, 24, 28};

    private static final String NEW_TITLE = "NEW TITLE";
    private static final String NEW_LINK = "NEW LINK";
    private static final String ACCESS_DATABASE_EXCEPTION_MESSAGE =
            "Some exception happened while trying to execute query.";

    private static final Pageable defaultPageable = of(ApplicationConstant.DEFAULT_PAGE_NUMBER, ApplicationConstant.DEFAULT_PAGE_SIZE);
    private static final Pageable allResultsPageable = of(ApplicationConstant.DEFAULT_PAGE_NUMBER, (int)QUESTIONS_NUMBER + 1);

    @Nested
    @DisplayName("QuestionService - findAll()")
    class FindAll extends AbstractUnitTest {

        @Test
        @DisplayName("Anonymous user has access")
        void findAll_notAuthenticated_withDefaultLocaleAndDefaultPageable() {

            //when
            Page<ReadAllQuestionDTO> questions =
                    questionService.findAll(null, LocaleEnum.EN.getValue(), defaultPageable);

            //then
            assertEquals(QUESTIONS_NUMBER, questions.getTotalElements());
            assertEquals(DEFAULT_TOTAL_PAGES, questions.getTotalPages());
            assertTrue(questions.isFirst());
            assertTrue(questions.hasNext());
            questions.get().forEach(question -> assertFalse(question::getIsOwner));
        }

        @Test
        @DisplayName("Anonymous user can change page parameters")
        void findAll_notAuthenticated_withDefaultLocaleAndAllResultsPageable() {

            //when
            Page<ReadAllQuestionDTO> questions =
                    questionService.findAll(null, LocaleEnum.EN.getValue(), allResultsPageable);

            //then
            assertEquals(QUESTIONS_NUMBER, questions.getTotalElements());
            assertEquals(1, questions.getTotalPages());
            assertTrue(questions.isFirst());
            assertFalse(questions.hasNext());
            questions.get().forEach(question -> assertFalse(question::getIsOwner));
        }

        @Test
        @DisplayName("Registered user has access")
        void findAll_authenticatedAsUser_withDefaultLocaleAndAllResultsPageable() {

            //given
            authenticateAsUser();

            //when
            Page<ReadAllQuestionDTO> questions =
                    questionService.findAll(null, LocaleEnum.EN.getValue(), allResultsPageable);

            //then
            assertEquals(QUESTIONS_NUMBER, questions.getTotalElements());
            assertEquals(1, questions.getTotalPages());
            assertTrue(questions.isFirst());
            assertFalse(questions.hasNext());
            assertTrue(questions.get().anyMatch(ReadAllQuestionDTO::getIsOwner));
        }

        @Test
        @DisplayName("Admin has access")
        void findAll_authenticatedAsAdmin_withDefaultLocaleAndAllResultsPageable() {

            //given
            authenticateAsAdmin();

            //when
            Page<ReadAllQuestionDTO> questions =
                    questionService.findAll(null, LocaleEnum.EN.getValue(), allResultsPageable);

            //then
            assertEquals(QUESTIONS_NUMBER, questions.getTotalElements());
            assertEquals(1, questions.getTotalPages());
            assertTrue(questions.isFirst());
            assertFalse(questions.hasNext());
            assertTrue(questions.get().anyMatch(ReadAllQuestionDTO::getIsOwner));
        }

        @Test
        @DisplayName("Translator has access")
        void findAll_authenticatedAsTranslator_withDefaultLocaleAndAllResultsPageable() {

            //given
            authenticateAsTranslator();

            //when
            Page<ReadAllQuestionDTO> questions =
                    questionService.findAll(null, LocaleEnum.EN.getValue(), allResultsPageable);

            //then
            assertEquals(QUESTIONS_NUMBER, questions.getTotalElements());
            assertEquals(1, questions.getTotalPages());
            assertTrue(questions.isFirst());
            assertFalse(questions.hasNext());
            assertTrue(questions.get().anyMatch(ReadAllQuestionDTO::getIsOwner));
        }

        @Test
        @DisplayName("Moderator has access")
        void findAll_authenticatedAsModerator_withDefaultLocaleAndAllResultsPageable() {

            //given
            authenticateAsModerator();

            //when
            Page<ReadAllQuestionDTO> questions =
                    questionService.findAll(null, LocaleEnum.EN.getValue(), allResultsPageable);

            //then
            assertEquals(QUESTIONS_NUMBER, questions.getTotalElements());
            assertEquals(1, questions.getTotalPages());
            assertTrue(questions.isFirst());
            assertFalse(questions.hasNext());
            assertTrue(questions.get().anyMatch(ReadAllQuestionDTO::getIsOwner));
        }
    }

    @Nested
    @DisplayName("QuestionService - findAllForTranslation()")
    class FindAllForTranslation extends AbstractUnitTest {

        @Test
        @DisplayName("Anonymous user has no access")
        void findAllForTranslation_notAuthenticated_withDefaultLocaleAndDefaultPageable() {

            //when
            assertThrows(AuthenticationCredentialsNotFoundException.class,
                    () -> questionService.findAllForTranslation(LocaleEnum.EN.getValue(), defaultPageable));
        }

        @Test
        @DisplayName("Registered user has no access")
        void findAllForTranslation_authenticatedAsUser_withDefaultLocaleAndAllResultsPageable() {

            //given
            authenticateAsUser();

            //when
            assertThrows(AccessDeniedException.class,
                    () -> questionService.findAllForTranslation(LocaleEnum.EN.getValue(), allResultsPageable));
        }

        @Test
        @DisplayName("Admin has no access")
        void findAllForTranslation_authenticatedAsAdmin_withDefaultLocaleAndAllResultsPageable() {

            //given
            authenticateAsAdmin();

            //when
            assertThrows(AccessDeniedException.class,
                    () -> questionService.findAllForTranslation(LocaleEnum.EN.getValue(), allResultsPageable));
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#provideCreateQuestionDTO")
        @DisplayName("Translator has access")
        void findAllForTranslation_authenticatedAsTranslator_withDefaultLocaleAndDefaultPageable(
                CreateQuestionDTO dto) {

            //given
            authenticateAsTranslator();
            questionService.save(dto);

            //when
            Page<ReadAllQuestionDTO> questions =
                    questionService.findAllForTranslation(LocaleEnum.EN.getValue(), defaultPageable);

            //then
            assertEquals(QUESTIONS_TO_TRANSLATE + 1, questions.getTotalElements());
            assertEquals(1, questions.getTotalPages());
            assertTrue(questions.isFirst());
            assertFalse(questions.hasNext());
            assertEquals(NEW_TITLE, questions.getContent().get(0).getTitle());
            assertEquals(NEW_LINK, questions.getContent().get(0).getLink());
            assertEquals(LevelType.SENIOR.name().toUpperCase(),
                    questions.getContent().get(0).getLevel().toUpperCase());
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#provideCreateQuestionDTO")
        @DisplayName("Translator can change page parameters")
        void findAllForTranslation_authenticatedAsTranslator_withDefaultLocaleAndAllResultsPageable(
                CreateQuestionDTO dto) {

            //given
            authenticateAsTranslator();
            questionService.save(dto);

            //when
            Page<ReadAllQuestionDTO> questions =
                    questionService.findAllForTranslation(LocaleEnum.EN.getValue(), allResultsPageable);

            //then
            assertEquals(QUESTIONS_TO_TRANSLATE + 1, questions.getTotalElements());
            assertEquals(1, questions.getTotalPages());
            assertTrue(questions.isFirst());
            assertFalse(questions.hasNext());
            assertEquals(NEW_TITLE, questions.getContent().get(0).getTitle());
            assertEquals(NEW_LINK, questions.getContent().get(0).getLink());
            assertEquals(LevelType.SENIOR.name().toUpperCase(),
                    questions.getContent().get(0).getLevel().toUpperCase());
        }

        @Test
        @DisplayName("Moderator has no access")
        void findAllForTranslation_authenticatedAsModerator_withDefaultLocaleAndAllResultsPageable() {

            //given
            authenticateAsModerator();

            //when
            assertThrows(AccessDeniedException.class,
                    () -> questionService.findAllForTranslation(LocaleEnum.EN.getValue(), allResultsPageable));
        }
    }

    @Nested
    @DisplayName("QuestionService - findById()")
    class FindById extends AbstractUnitTest {

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#differentOwnerQuestionIds")
        @DisplayName("Anonymous user has no access")
        void findById_notAuthenticated_hasNoAccess(long id) {

            //when
            assertThrows(AuthenticationCredentialsNotFoundException.class,
                    () -> questionService.findById(id));
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#userQuestionIds")
        @DisplayName("Registered user has access to own questions")
        void findById_authenticatedAsUser_hasAccessToOwnQuestions(long id) {

            //given
            authenticateAsUser();
            Long userId = ((SecurityPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUserId();

            //when
            Question question = questionService.findById(id);

            //then
            assertEquals(id, question.getId());
            assertEquals(userId, question.getUser().getId());
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#adminQuestionIds")
        @DisplayName("Registered user has no access to not own questions")
        void findById_authenticatedAsUser_hasNoAccessToNotOwnQuestions(long id) {

            //given
            authenticateAsUser();

            //when
            assertThrows(AccessDeniedException.class, () -> questionService.findById(id));
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#adminQuestionIds")
        @DisplayName("Admin has access to own questions")
        void findById_authenticatedAsAdmin_hasAccessToOwnQuestions(long id) {

            //given
            authenticateAsAdmin();
            Long userId = ((SecurityPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUserId();

            //when
            Question question = questionService.findById(id);

            //then
            assertEquals(id, question.getId());
            assertEquals(userId, question.getUser().getId());
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#differentOwnerQuestionIds")
        @DisplayName("Admin has access to not own questions")
        void findById_authenticatedAsAdmin_hasAccessToNotOwnQuestions(long id) {

            //given
            authenticateAsAdmin();

            //when
            Question question = questionService.findById(id);

            //then
            assertEquals(id, question.getId());
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#translatorQuestionIds")
        @DisplayName("Translator has access to own questions")
        void findById_authenticatedAsTranslator_hasAccessToOwnQuestions(long id) {

            //given
            authenticateAsTranslator();
            Long userId = ((SecurityPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUserId();

            //when
            Question question = questionService.findById(id);

            //then
            assertEquals(id, question.getId());
            assertEquals(userId, question.getUser().getId());
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#differentOwnerQuestionIds")
        @DisplayName("Translator has access to not own questions")
        void findById_authenticatedAsTranslator_hasAccessToNotOwnQuestions(long id) {

            //given
            authenticateAsTranslator();

            //when
            Question question = questionService.findById(id);

            //then
            assertEquals(id, question.getId());
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#moderatorQuestionIds")
        @DisplayName("Moderator has access to own questions")
        void findById_authenticatedAsModerator_hasAccessToOwnQuestions(long id) {

            //given
            authenticateAsModerator();
            Long userId = ((SecurityPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUserId();

            //when
            Question question = questionService.findById(id);

            //then
            assertEquals(id, question.getId());
            assertEquals(userId, question.getUser().getId());
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#differentOwnerQuestionIds")
        @DisplayName("Moderator has access to not own questions")
        void findById_authenticatedAsModerator_hasAccessToNotOwnQuestions(long id) {

            //given
            authenticateAsModerator();

            //when
            Question question = questionService.findById(id);

            //then
            assertEquals(id, question.getId());
        }

        @Test
        @DisplayName("ACL should throw internal server error when questionId is null")
        void findById_withAnyAuthentication_shouldThrowExceptionWhenIdIsNull() {

            //Given
            authenticateAsUser();

            //when
            SamsDemoException exception =
                    assertThrows(SamsDemoException.class, () -> questionService.findById(null));

            //then
            assertEquals(ErrorCode.UNEXPECTED_AUTHENTICATION_ERROR, exception.getMessage());
            assertEquals(INTERNAL_SERVER_ERROR, exception.getStatus());
        }

        @Test
        @DisplayName("ACL should throw not found exception when questionId not exists")
        void findById_withAnyAuthentication_shouldThrowExceptionWhenIdNotExists() {

            //Given
            authenticateAsAdmin();

            //when
            SamsDemoException exception = assertThrows(SamsDemoException.class,
                    () -> questionService.findById(-ADMIN_QUESTION_IDS[0]));

            //then
            assertEquals(ErrorCode.ENTITY_NOT_FOUND, exception.getMessage());
            assertEquals(NOT_FOUND, exception.getStatus());
        }
    }

    @Nested
    @DisplayName("QuestionService - findByIdAndByPassProxy() - used by ACL")
    class FindByIdAndByPassProxy extends AbstractUnitTest {

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#differentOwnerQuestionIds")
        @DisplayName("ACL should by pass proxy and have access")
        void findByIdAndByPassProxy_notAuthenticated_hasAccess(long id) {

            //when
            Question question = questionService.findByIdAndByPassProxy(id);

            //then
            assertEquals(id, question.getId());
        }

        @Test
        @DisplayName("ACL should throw bad request when questionId is null")
        void findByIdAndByPassProxy_notAuthenticated_hasAccess() {

            //when
            SamsDemoException exception = assertThrows(SamsDemoException.class,
                    () -> questionService.findByIdAndByPassProxy(null));

            //then
            assertEquals(ErrorCode.ID_MISSING, exception.getMessage());
            assertEquals(BAD_REQUEST, exception.getStatus());
        }
    }

    @Nested
    @DisplayName("QuestionService - save()")
    class Save extends AbstractUnitTest {

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#provideCreateQuestionDTO")
        @DisplayName("Anonymous user has no access")
        void save_notAuthenticated_hasNoAccess(CreateQuestionDTO dto) {

            //when
            assertThrows(AuthenticationCredentialsNotFoundException.class,
                    () -> questionService.save(dto));
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#provideCreateQuestionDTO")
        @DisplayName("Registered user has access")
        void save_authenticatedAsUser_hasAccess(CreateQuestionDTO dto) {

            //given
            authenticateAsUser();
            Long userId = ((SecurityPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUserId();

            //when
            Question question = questionService.save(dto);

            //then
            assertEquals(dto.getTitle(), question.getTitles().get(0).getTitle());
            assertEquals(dto.getLocale().getValue(), question.getTitles().get(0).getLocale().getCode());
            assertEquals(dto.getLink(), question.getLink());
            assertEquals(dto.getLevel(), question.getLevel().getType());
            assertEquals(userId, question.getUser().getId());
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#provideCreateQuestionDTO")
        @DisplayName("Admin has access")
        void save_authenticatedAsAdmin_hasAccess(CreateQuestionDTO dto) {

            //given
            authenticateAsAdmin();
            Long userId = ((SecurityPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUserId();

            //when
            Question question = questionService.save(dto);

            //then
            assertEquals(dto.getTitle(), question.getTitles().get(0).getTitle());
            assertEquals(dto.getLocale().getValue(), question.getTitles().get(0).getLocale().getCode());
            assertEquals(dto.getLink(), question.getLink());
            assertEquals(dto.getLevel(), question.getLevel().getType());
            assertEquals(userId, question.getUser().getId());
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#provideCreateQuestionDTO")
        @DisplayName("Translator has access")
        void save_authenticatedAsTranslator_hasAccess(CreateQuestionDTO dto) {

            //given
            authenticateAsTranslator();
            Long userId = ((SecurityPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUserId();

            //when
            Question question = questionService.save(dto);

            //then
            assertEquals(dto.getTitle(), question.getTitles().get(0).getTitle());
            assertEquals(dto.getLocale().getValue(), question.getTitles().get(0).getLocale().getCode());
            assertEquals(dto.getLink(), question.getLink());
            assertEquals(dto.getLevel(), question.getLevel().getType());
            assertEquals(userId, question.getUser().getId());
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#provideCreateQuestionDTO")
        @DisplayName("Moderator has access")
        void save_authenticatedAsModerator_hasAccess(CreateQuestionDTO dto) {

            //given
            authenticateAsModerator();
            Long userId = ((SecurityPrincipal) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal()).getUserId();

            //when
            Question question = questionService.save(dto);

            //then
            assertEquals(dto.getTitle(), question.getTitles().get(0).getTitle());
            assertEquals(dto.getLocale().getValue(), question.getTitles().get(0).getLocale().getCode());
            assertEquals(dto.getLink(), question.getLink());
            assertEquals(dto.getLevel(), question.getLevel().getType());
            assertEquals(userId, question.getUser().getId());
        }
    }

    @Nested
    class Delete extends AbstractUnitTest {

        @Test
        void delete_test() {

            authenticateAsAdmin();

            questionService.delete(1L);

            System.out.println(questionService.findAll(null, "en-US", of(0, 30)).getTotalElements());
        }
    }

    @Nested
    @DisplayName("Exceptions while executing DB query")
    @Import(TestBeanConfiguration.class)
    class SimulateExceptions extends AbstractUnitTest {

        @Autowired
        private QuestionService questionServiceWithMock;

        @Autowired
        private QuestionRepository mockQuestionRepository;

        @Autowired
        private LevelConRepository mockLevelConRepository;

        @Autowired
        private UserRepository mockUserRepository;

        @Test
        void findAll_throwsException() {

            //Given
            when(mockQuestionRepository
                    .findAll(
                            eq(null),
                            ArgumentMatchers.eq(LocaleEnum.EN.getValue()),
                            eq(ApplicationConstant.ANONYMOUS_USER_ID),
                            eq(defaultPageable)))
                    .thenThrow(new RuntimeException(ACCESS_DATABASE_EXCEPTION_MESSAGE));

            //when
            SamsDemoException exception = assertThrows(SamsDemoException.class,
                    () -> questionServiceWithMock.findAll(null, LocaleEnum.EN.getValue(), defaultPageable));

            //then
            assertEquals(ErrorCode.ACCESS_DATABASE_ERROR, exception.getMessage());
            assertEquals(INTERNAL_SERVER_ERROR, exception.getStatus());
        }

        @Test
        void findAllForTranslation_throwsException() {

            //Given
            User user = mockUser(translatorCreds, TRANSLATOR_QUESTION_IDS, USER, TRANSLATOR);

            when(mockUserRepository
                    .findByEmail(
                            eq(user.getEmail())))
                    .thenReturn(user);

            authenticateAsTranslator();

            when(mockQuestionRepository
                    .findAllForTranslation(
                            ArgumentMatchers.eq(LocaleEnum.EN.getValue()),
                            eq(defaultPageable)))
                    .thenThrow(new RuntimeException(ACCESS_DATABASE_EXCEPTION_MESSAGE));

            //when
            SamsDemoException exception = assertThrows(SamsDemoException.class,
                    () -> questionServiceWithMock.findAllForTranslation(LocaleEnum.EN.getValue(), defaultPageable));

            //then
            assertEquals(ErrorCode.ACCESS_DATABASE_ERROR, exception.getMessage());
            assertEquals(INTERNAL_SERVER_ERROR, exception.getStatus());
        }

        @Test
        void findById_throwsException() {

            //Given
            User user = mockUser(userCreds, USER_QUESTION_IDS, USER);

            when(mockUserRepository
                    .findByEmail(
                            eq(user.getEmail())))
                    .thenReturn(user);

            authenticateAsUser();

            when(mockQuestionRepository
                    .findById(
                            eq(USER_QUESTION_IDS[0])))
                    .thenThrow(new RuntimeException(ACCESS_DATABASE_EXCEPTION_MESSAGE));

            //when
            SamsDemoException exception = assertThrows(SamsDemoException.class,
                    () -> questionServiceWithMock.findById(USER_QUESTION_IDS[0]));

            //then
            assertEquals(ErrorCode.ACCESS_DATABASE_ERROR, exception.getMessage());
            assertEquals(INTERNAL_SERVER_ERROR, exception.getStatus());
        }

        @ParameterizedTest
        @MethodSource("com.github.alexkhromov.service.impl.QuestionServiceTest#provideCreateQuestionDTO")
        void save_throwsException_whenSearchLevel(CreateQuestionDTO dto) {

            //Given
            User user = mockUser(userCreds, USER_QUESTION_IDS, USER);

            when(mockUserRepository
                    .findByEmail(
                            eq(user.getEmail())))
                    .thenReturn(user);

            authenticateAsUser();

            when(mockLevelConRepository
                    .findByType(
                            eq(dto.getLevel())))
                    .thenThrow(new RuntimeException(ACCESS_DATABASE_EXCEPTION_MESSAGE));

            //when
            SamsDemoException exception = assertThrows(SamsDemoException.class,
                    () -> questionServiceWithMock.save(dto));

            //then
            assertEquals(ErrorCode.ACCESS_DATABASE_ERROR, exception.getMessage());
            assertEquals(INTERNAL_SERVER_ERROR, exception.getStatus());
        }
    }

    static LongStream differentOwnerQuestionIds() {
        return LongStream.of(
                USER_QUESTION_IDS[0],
                ADMIN_QUESTION_IDS[0],
                TRANSLATOR_QUESTION_IDS[0],
                MODERATOR_QUESTION_IDS[0]);
    }

    static LongStream userQuestionIds() {
        return LongStream.of(USER_QUESTION_IDS);
    }

    static LongStream adminQuestionIds() {
        return LongStream.of(ADMIN_QUESTION_IDS);
    }

    static LongStream translatorQuestionIds() {
        return LongStream.of(TRANSLATOR_QUESTION_IDS);
    }

    static LongStream moderatorQuestionIds() {
        return LongStream.of(MODERATOR_QUESTION_IDS);
    }

    static Stream<CreateQuestionDTO> provideCreateQuestionDTO() {

        CreateQuestionDTO dto = new CreateQuestionDTO();
        dto.setTitle(NEW_TITLE);
        dto.setLink(NEW_LINK);
        dto.setLocale(LocaleEnum.EN);
        dto.setLevel(LevelType.SENIOR);

        return Stream.of(dto);
    }
}