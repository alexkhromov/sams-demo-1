package com.github.alexkhromov.model.dto;

import com.github.alexkhromov.model.enums.LevelType;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class ReadQuestionDTO extends BaseDTO {

    private Long id;
    private String link;
    private LevelType level;
    private List<TitleDTO> titles;
    private Boolean isOwner;
}