package com.github.alexkhromov.security;

import com.github.alexkhromov.model.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
public final class SecurityPrincipal extends
        org.springframework.security.core.userdetails.User {

    private Long userId;

    public SecurityPrincipal(User user, List<GrantedAuthority> authorities) {

        super(user.getEmail(), user.getPassword(), authorities);
        this.userId = user.getId();
    }
}