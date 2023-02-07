package com.github.alexkhromov.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.alexkhromov.model.dto.deserializer.LevelTypeDeserializer;
import com.github.alexkhromov.model.enums.LevelType;
import com.github.alexkhromov.model.error.ErrorCode;
import com.github.alexkhromov.model.dto.validator.annotation.EnumType;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class UpdateQuestionDTO extends BaseDTO {

    @NotNull(message = ErrorCode.FIELD_MISSING)
    @NotBlank(message = ErrorCode.FIELD_EMPTY)
    @Size(min = 3, max = 255, message = ErrorCode.FIELD_INVALID_LENGTH)
    private String link;

    @NotNull(message = ErrorCode.FIELD_MISSING)
    @EnumType(enumClass = LevelType.class, message = ErrorCode.FIELD_INVALID_VALUE)
    @JsonDeserialize(using = LevelTypeDeserializer.class)
    private LevelType level;

    @NotNull(message = ErrorCode.FIELD_MISSING)
    @NotEmpty(message = ErrorCode.COLLECTION_EMPTY)
    @Valid
    private List<TitleDTO> titles;
}