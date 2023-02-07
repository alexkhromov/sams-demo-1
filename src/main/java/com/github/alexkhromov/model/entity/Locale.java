package com.github.alexkhromov.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "LOCALE")
@Data
@EqualsAndHashCode(of = {"id", "code"}, callSuper = false)
@ToString(of = {"id", "code"})
public class Locale extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "LOCALE_ID", nullable = false)
    private Long id;

    @Column(name = "LOCALE_CODE", nullable = false)
    private String code;
}