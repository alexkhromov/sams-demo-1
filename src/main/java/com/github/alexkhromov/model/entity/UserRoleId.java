package com.github.alexkhromov.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@EqualsAndHashCode(of = {"userId", "roleId"})
@ToString(of = {"userId", "roleId"})
public class UserRoleId implements Serializable {

    @Column(name = "USER_ID", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "ROLE_ID", nullable = false, updatable = false)
    private Long roleId;
}