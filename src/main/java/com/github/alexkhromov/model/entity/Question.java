package com.github.alexkhromov.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "QUESTION")
@Data
@EqualsAndHashCode(of = {"id", "link", "level"}, callSuper = false)
@ToString(of = {"id", "link", "level"})
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "QUESTION_ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "LINK", nullable = false)
    private String link;

    @ManyToOne
    @JoinColumn(name = "LEVEL_ID")
    private LevelCon level;

    @OneToMany(mappedBy = "question", cascade = ALL)
    private List<Title> titles;

    @Column(name = "IS_FULLY_LOCALIZED", nullable = false)
    private Boolean isFullyLocalized;
}