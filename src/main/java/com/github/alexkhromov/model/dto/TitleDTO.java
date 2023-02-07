package com.github.alexkhromov.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.alexkhromov.model.enums.LocaleEnum;
import com.github.alexkhromov.model.error.ErrorCode;
import com.github.alexkhromov.model.dto.deserializer.LocaleDeserializer;
import com.github.alexkhromov.model.dto.validator.annotation.EnumType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class TitleDTO extends BaseDTO {

    @NotNull(message = ErrorCode.FIELD_MISSING)
    @NotBlank(message = ErrorCode.FIELD_EMPTY)
    @Size(min = 3, max = 255, message = ErrorCode.FIELD_INVALID_LENGTH)
    private String title;

    @NotNull(message = ErrorCode.FIELD_MISSING)
    @EnumType(enumClass = LocaleEnum.class, message = ErrorCode.FIELD_INVALID_VALUE)
    @JsonDeserialize(using = LocaleDeserializer.class)
    private LocaleEnum locale;
}