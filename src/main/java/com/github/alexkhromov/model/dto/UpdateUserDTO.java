package com.github.alexkhromov.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.alexkhromov.common.ApplicationConstant;
import com.github.alexkhromov.model.dto.deserializer.RoleDeserializer;
import com.github.alexkhromov.model.enums.Role;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static com.github.alexkhromov.model.error.ErrorCode.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class UpdateUserDTO extends BaseDTO {

    @NotNull(message = FIELD_MISSING)
    @NotBlank(message = FIELD_EMPTY)
    @Email(regexp = ApplicationConstant.EMAIL_PATTERN, message = EMAIL_NOT_VALID)
    private String email;

    @NotNull(message = FIELD_MISSING)
    @NotBlank(message = FIELD_EMPTY)
    @Size(min = 3, max = 255, message = FIELD_INVALID_LENGTH)
    private String username;

    @JsonDeserialize(using = RoleDeserializer.class)
    private List<Role> roles;

    private Boolean isDeleted;
}