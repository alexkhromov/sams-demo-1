package com.github.alexkhromov.model.entity;

import com.github.alexkhromov.model.entity.listener.UserListener;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "USER")
@SQLDelete(sql = "UPDATE USER SET IS_DELETED = 'Y' WHERE USER_ID = ?")
@EntityListeners(UserListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id", "username"}, callSuper = false)
@ToString(of = {"id", "username"})
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    private Long id;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "IS_DELETED", nullable = false)
    @Type(type = "yes_no")
    private Boolean isDeleted = false;

    @OneToMany(fetch = EAGER, mappedBy = "user", cascade = ALL)
    private List<UserRole> roles;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Question> questions;
}