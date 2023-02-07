package com.github.alexkhromov.common;

public interface ApplicationConstant {

    String PAGE_NUMBER_PARAMETER = "pageNum";
    int DEFAULT_PAGE_NUMBER = 0;

    String PAGE_SIZE_PARAMETER = "pageSize";
    int DEFAULT_PAGE_SIZE = 5;

    String LEVEL_PARAMETER = "level";

    int SUPPORTED_LOCALES = 2;

    String QUESTION_ENTITY_LOCATION = "/api/v1/questions/{questionId}";
    String USER_ENTITY_LOCATION = "/api/v1/users/{userId}";

    String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    String BEARER_PREFIX = "Bearer ";

    Long ANONYMOUS_USER_ID = -1L;
}