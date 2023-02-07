package com.github.alexkhromov.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "TITLE")
@Data
@EqualsAndHashCode(of = {"title"}, callSuper = false)
@ToString(of = {"title"})
public class Title extends BaseEntity {

    @EmbeddedId
    private TitleId id;

    @ManyToOne
    @MapsId("localeId")
    @JoinColumn(name = "LOCALE_ID", updatable = false, insertable = false)
    private Locale locale;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "QUESTION_ID", updatable = false, insertable = false)
    private Question question;

    @Column(name = "TITLE", nullable = false)
    private String title;
}