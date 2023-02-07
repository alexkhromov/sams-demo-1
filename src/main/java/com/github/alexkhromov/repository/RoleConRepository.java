package com.github.alexkhromov.repository;

import com.github.alexkhromov.model.entity.RoleCon;
import com.github.alexkhromov.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleConRepository extends JpaRepository<RoleCon, Long> {

    RoleCon findByRole(Role role);
}