package com.github.alexkhromov.controller;

import com.github.alexkhromov.service.IAuthenticationService;
import com.github.alexkhromov.model.dto.security.SignInRequest;
import com.github.alexkhromov.model.dto.security.SignUpRequest;
import com.github.alexkhromov.model.entity.User;
import com.github.alexkhromov.model.error.exception.SamsDemoException;
import com.github.alexkhromov.model.response.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.github.alexkhromov.common.ApplicationConstant.USER_ENTITY_LOCATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class LoginController {

    private AuthenticationManager authenticationManager;
    private IAuthenticationService authenticationService;

    @Autowired
    public LoginController(
            AuthenticationManager authenticationManager,
            IAuthenticationService authenticationService) {

        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Valid SignUpRequest signUpRequest) throws SamsDemoException {

        User user = authenticationService.signUp(authenticationManager, signUpRequest);

        return ResponseBuilder
                .empty()
                .withLocation(USER_ENTITY_LOCATION, user.getId())
                .withAuthorization(authenticationService.signIn(authenticationManager))
                .withHttpStatus(CREATED)
                .build();
    }

    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody @Valid SignInRequest signInRequest) throws SamsDemoException {

        return ResponseBuilder
                .empty()
                .withAuthorization(authenticationService.signIn(authenticationManager, signInRequest))
                .withHttpStatus(OK)
                .build();
    }
}