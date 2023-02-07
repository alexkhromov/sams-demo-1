package com.github.alexkhromov.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "USER_ROLE")
@Data
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@ToString(of = {"id"})
public class UserRole extends BaseEntity implements Serializable {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "USER_ID", updatable = false, insertable = false)
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "ROLE_ID", updatable = false, insertable = false)
    private RoleCon roleCon;
}