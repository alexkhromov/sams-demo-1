package com.github.alexkhromov.service;

import com.github.alexkhromov.model.dto.security.SignInRequest;
import com.github.alexkhromov.model.dto.security.SignUpRequest;
import com.github.alexkhromov.model.entity.User;
import com.github.alexkhromov.model.error.exception.SamsDemoException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAuthenticationService extends UserDetailsService {

    User signUp(AuthenticationManager authenticationManager,
                SignUpRequest signUpRequest) throws SamsDemoException;

    String signIn(AuthenticationManager authenticationManager,
                  SignInRequest signInRequest) throws SamsDemoException;

    String signIn(AuthenticationManager authenticationManager) throws SamsDemoException;

    boolean checkQuestionOwnerShip(Authentication authentication, Long questionId);

    boolean checkUserOwnerShip(Authentication authentication, Long userId);
}