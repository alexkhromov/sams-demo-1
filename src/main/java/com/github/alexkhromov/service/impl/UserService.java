package com.github.alexkhromov.service.impl;

import com.github.alexkhromov.repository.RoleConRepository;
import com.github.alexkhromov.repository.UserRepository;
import com.github.alexkhromov.service.IUserService;
import com.github.alexkhromov.model.dto.UpdateUserDTO;
import com.github.alexkhromov.model.entity.User;
import com.github.alexkhromov.model.entity.UserRole;
import com.github.alexkhromov.model.entity.UserRoleId;
import com.github.alexkhromov.model.error.exception.SamsDemoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.alexkhromov.model.error.ErrorCode.*;
import static com.github.alexkhromov.model.error.exception.SamsDemoException.*;
import static com.github.alexkhromov.security.SecurityExpression.USER_ACL;

@Slf4j
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleConRepository roleConRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleConRepository roleConRepository) {
        this.userRepository = userRepository;
        this.roleConRepository = roleConRepository;
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> findAll(Pageable pageable) {

        log.debug("Entered [findAll] with pageable = {}", pageable);

        try {
            return userRepository.findAll(pageable);
        } catch (Exception ex) {

            log.error("Internal server exception [findAll]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize(USER_ACL)
    public User findById(Long userId) throws SamsDemoException {

        log.debug("Entered [findById] with userId = {}", userId);

        if (userId == null) {

            log.error("Bad request exception [findById]: userId is missing");
            throw badRequestException(ID_MISSING);
        }

        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(userId);
        } catch (Exception ex) {

            log.error("Internal server exception [findById]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }

        if (!optionalUser.isPresent()) {

            log.error("Entity not found exception [findById]: {}, {}",
                    User.class.getSimpleName(), userId.toString());

            throw entityNotFoundException(
                    ENTITY_NOT_FOUND,
                    User.class.getSimpleName(),
                    userId.toString());
        }

        log.debug("Exited [findById] with user = {}", optionalUser.get());

        return optionalUser.get();
    }

    @Override
    public User findByIdAndByPassProxy(Long userId) throws SamsDemoException {

        log.debug("Entered [findByIdAndByPassProxy] with userId = {}", userId);

        return this.findById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) throws SamsDemoException {

        log.debug("Entered [findByEmail] with email = {}", email);

        try {
            return userRepository.findByEmail(email);
        } catch (Exception ex) {

            log.error("Internal server exception [findByEmail]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }
    }

    @Override
    @Transactional
    @PreAuthorize(USER_ACL)
    public User update(Long userId, UpdateUserDTO userDTO) throws SamsDemoException {

        log.debug("Entered [update] with userId = {}, userDTO = {}", userId, userDTO);

        User user = findById(userId);

        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setRoles(userDTO
                .getRoles()
                .stream()
                .map(roleConRepository::findByRole)
                .map(roleCon -> {
                    UserRole userRole = new UserRole();
                    userRole.setId(new UserRoleId());
                    userRole.getId().setUserId(userId);
                    userRole.getId().setRoleId(roleCon.getId());
                    userRole.setRoleCon(roleCon);
                    userRole.setUser(user);
                    return userRole;
                })
                .collect(Collectors.toList())
        );
        user.setIsDeleted(userDTO.getIsDeleted());

        try {
            return userRepository.save(user);
        } catch (Exception ex) {

            log.error("Internal server exception [update]: database access error {}", ex.getMessage());
            throw internalServerException(ACCESS_DATABASE_ERROR);
        }
    }

    @Override
    @PreAuthorize(USER_ACL)
    public void delete(Long userId) throws SamsDemoException {

        log.debug("Entered [delete]");

        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException ex) {

            log.error("Entity not found exception [delete]: {}, {}",
                    User.class.getSimpleName(), userId.toString());

            throw entityNotFoundException(
                    ENTITY_NOT_FOUND,
                    User.class.getSimpleName(),
                    userId.toString());
        }
    }
}