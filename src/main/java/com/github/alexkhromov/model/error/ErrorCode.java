package com.github.alexkhromov.model.error;

public interface ErrorCode {

    String FIELD_MISSING = "field.missing";
    String FIELD_EMPTY = "field.empty";
    String FIELD_INVALID_VALUE = "field.invalid.value";
    String FIELD_INVALID_LENGTH = "field.invalid.length";

    String COLLECTION_EMPTY = "collection.empty";

    String EMAIL_NOT_VALID = "email.not.valid";

    String ID_MISSING = "id.missing";
    String ACCESS_DATABASE_ERROR = "access.database.error";
    String ENTITY_NOT_FOUND = "entity.not.found";

    String LOCALE_NOT_SUPPORTED = "locale.not.supported";
    String PROCESS_LOCALE_ERROR = "process.locale.error";
    String LOCALE_INVALID = "locale.invalid";

    String LEVEL_TYPE_INVALID = "level.invalid";

    String ROLE_INVALID = "role.invalid";

    String UNEXPECTED_ERROR = "unexpected.error";
    String UNEXPECTED_AUTHENTICATION_ERROR = "unexpected.authentication.error";

    String USER_EXISTS = "user.exists";

    String BAD_CREDENTIALS_ERROR = "bad.credentials.error";

    String SESSION_EXPIRED = "session.expired";

    String TOKEN_MISSING = "token.missing";

    String ACCESS_DENIED = "access.denied";
}