package com.github.alexkhromov.model.dto.security;

import com.github.alexkhromov.common.ApplicationConstant;
import com.github.alexkhromov.model.error.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(of = {"email", "username"})
@ToString(of = {"email", "username"})
public class SignUpRequest {

    @NotNull(message = ErrorCode.FIELD_MISSING)
    @NotBlank(message = ErrorCode.FIELD_EMPTY)
    @Email(regexp = ApplicationConstant.EMAIL_PATTERN, message = ErrorCode.EMAIL_NOT_VALID)
    private String email;

    @NotNull(message = ErrorCode.FIELD_MISSING)
    @NotBlank(message = ErrorCode.FIELD_EMPTY)
    @Size(min = 3, max = 255, message = ErrorCode.FIELD_INVALID_LENGTH)
    private String username;

    @NotNull(message = ErrorCode.FIELD_MISSING)
    @NotBlank(message = ErrorCode.FIELD_EMPTY)
    @Size(min = 3, max = 255, message = ErrorCode.FIELD_INVALID_LENGTH)
    private String password;
}