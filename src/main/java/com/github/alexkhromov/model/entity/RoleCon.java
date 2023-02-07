package com.github.alexkhromov.model.entity;

import com.github.alexkhromov.model.enums.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "ROLE")
@Data
@EqualsAndHashCode(of = {"id", "role"}, callSuper = false)
@ToString(of = {"id", "role"})
public class RoleCon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ROLE_ID", nullable = false)
    private Long id;

    @Enumerated(STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;
}