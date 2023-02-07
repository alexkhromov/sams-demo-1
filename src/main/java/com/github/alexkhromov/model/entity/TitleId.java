package com.github.alexkhromov.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@EqualsAndHashCode(of = {"localeId", "questionId"})
@ToString(of = {"localeId", "questionId"})
public class TitleId implements Serializable {

    @Column(name = "LOCALE_ID", nullable = false, updatable = false)
    private Long localeId;

    @Column(name = "QUESTION_ID", nullable = false, updatable = false)
    private Long questionId;
}