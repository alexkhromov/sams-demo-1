package com.github.alexkhromov.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@EqualsAndHashCode(of = {"levelId", "localeId"})
@ToString(of = {"levelId", "localeId"})
public class LevelLocalizedId implements Serializable {

    @Column(name = "LEVEL_ID")
    private Long levelId;

    @Column(name = "LOCALE_ID")
    private Long localeId;
}