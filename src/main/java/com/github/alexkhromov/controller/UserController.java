package com.github.alexkhromov.controller;

import com.github.alexkhromov.model.dto.ReadUserDTO;
import com.github.alexkhromov.model.dto.UpdateUserDTO;
import com.github.alexkhromov.model.entity.User;
import com.github.alexkhromov.model.error.exception.SamsDemoException;
import com.github.alexkhromov.model.mapper.IDTOMapper;
import com.github.alexkhromov.model.response.ResponseBuilder;
import com.github.alexkhromov.model.response.SamsDemoResponse;
import com.github.alexkhromov.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private IUserService userService;
    private IDTOMapper<ReadUserDTO, User> readUserDTOMapper;

    @Autowired
    public UserController(IUserService userService,
                          IDTOMapper<ReadUserDTO, User> readUserDTOMapper) {

        this.userService = userService;
        this.readUserDTOMapper = readUserDTOMapper;
    }

    @GetMapping
    public ResponseEntity<SamsDemoResponse<ReadUserDTO>> findAllUsers(
            Pageable pageable) throws SamsDemoException {

        Page<User> page = userService.findAll(pageable);

        return ResponseBuilder
                .<ReadUserDTO, User>success()
                .withPageData(page, readUserDTOMapper)
                .withHttpStatus(OK)
                .build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<SamsDemoResponse<ReadUserDTO>> findUserById(
            @PathVariable(name = "userId") Long userId) throws SamsDemoException {

        User user = userService.findById(userId);

        return ResponseBuilder
                .<ReadUserDTO, User>success()
                .withData(singletonList(user), readUserDTOMapper)
                .withHttpStatus(OK)
                .build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<SamsDemoResponse<ReadUserDTO>> updateUser(
            @PathVariable(name = "userId") Long userId,
            @RequestBody @Valid UpdateUserDTO userDTO) throws SamsDemoException {

        User user = userService.update(userId, userDTO);

        return ResponseBuilder
                .<ReadUserDTO, User>success()
                .withData(singletonList(user), readUserDTOMapper)
                .withHttpStatus(OK)
                .build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(
            @PathVariable(name = "userId") Long userId) throws SamsDemoException{

        userService.delete(userId);

        return ResponseBuilder
                .empty()
                .withHttpStatus(NO_CONTENT)
                .build();
    }
}