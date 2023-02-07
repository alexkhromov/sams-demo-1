package com.github.alexkhromov.service;

import com.github.alexkhromov.model.entity.Question;
import com.github.alexkhromov.model.entity.RoleCon;
import com.github.alexkhromov.model.entity.User;
import com.github.alexkhromov.model.enums.Role;
import com.github.alexkhromov.model.error.exception.SamsDemoException;

public interface IAuthenticationFacade {

    Question findQuestion(Long questionId) throws SamsDemoException;

    User findUser(Long userId) throws SamsDemoException;

    User findUser(String email) throws SamsDemoException;

    User save(User user) throws SamsDemoException;

    RoleCon findRoleCon(Role role) throws SamsDemoException;
}