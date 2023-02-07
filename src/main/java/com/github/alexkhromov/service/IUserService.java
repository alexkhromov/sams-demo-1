package com.github.alexkhromov.service;

import com.github.alexkhromov.model.dto.UpdateUserDTO;
import com.github.alexkhromov.model.entity.User;
import com.github.alexkhromov.model.error.exception.SamsDemoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    Page<User> findAll(Pageable pageable);

    User findById(Long userId) throws SamsDemoException;

    User findByIdAndByPassProxy(Long userId) throws SamsDemoException;

    User findByEmail(String email) throws SamsDemoException;

    User update(Long userId, UpdateUserDTO userDTO) throws SamsDemoException;

    void delete(Long userId) throws SamsDemoException;
}