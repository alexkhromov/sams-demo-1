package com.github.alexkhromov.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "LEVEL_LOCALIZED")
@Data
@EqualsAndHashCode(of = {"levelLocalized"}, callSuper = false)
@ToString(of = {"levelLocalized"})
public class LevelLocalized extends BaseEntity {

    @EmbeddedId
    private LevelLocalizedId levelLocalizedId;

    @ManyToOne
    @MapsId("levelId")
    @JoinColumn(name = "LEVEL_ID", updatable = false, insertable = false)
    private LevelCon level;

    @ManyToOne
    @MapsId("localeId")
    @JoinColumn(name = "LOCALE_ID", updatable = false, insertable = false)
    private Locale locale;

    @Column(name = "LEVEL_LOCALIZED", nullable = false, length = 10)
    private String levelLocalized;
}