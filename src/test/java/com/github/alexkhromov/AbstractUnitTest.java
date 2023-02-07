package com.github.alexkhromov;

import com.github.alexkhromov.service.impl.QuestionService;
import com.github.alexkhromov.model.entity.Question;
import com.github.alexkhromov.model.entity.RoleCon;
import com.github.alexkhromov.model.entity.User;
import com.github.alexkhromov.model.entity.UserRole;
import com.github.alexkhromov.model.enums.Role;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest(classes = {DemoApplication.class})
@SqlGroup({
        @Sql(scripts = {"/sql/schema.sql", "/sql/data.sql"}),
        @Sql(scripts = "/sql/drop.sql", executionPhase = AFTER_TEST_METHOD)
})
public abstract class AbstractUnitTest {

    protected Pair<String, String> userCreds =
            new ImmutablePair<>("user@demo.com", "user");
    protected Pair<String, String> adminCreds =
            new ImmutablePair<>("admin@demo.com", "admin");
    protected Pair<String, String> moderatorCreds =
            new ImmutablePair<>("moderator@demo.com", "moderator");
    protected Pair<String, String> translatorCreds =
            new ImmutablePair<>("translator@demo.com", "translator");

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    protected QuestionService questionService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @AfterEach
    public void cleanUpAuthentication() {

        SecurityContextHolder.getContext().setAuthentication(null);
    }

    protected void authenticateAsUser() {
        authenticate(userCreds.getKey(), userCreds.getValue());
    }

    protected void authenticateAsAdmin() {
        authenticate(adminCreds.getKey(), adminCreds.getValue());
    }

    protected void authenticateAsModerator() {
        authenticate(moderatorCreds.getKey(), moderatorCreds.getValue());
    }

    protected void authenticateAsTranslator() {
        authenticate(translatorCreds.getKey(), translatorCreds.getValue());
    }

    private void authenticate(String email, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected User mockUser(Pair<String, String> creds, long[] questionIds, Role... roles) {

        List<UserRole> userRoles = Stream.of(roles)
                .map(this::mapToUserRole)
                .collect(toList());

        List<Question> questions = LongStream.of(questionIds)
                .boxed()
                .map(this::mapToQuestion)
                .collect(toList());

        return User.builder()
                .id(questionIds[0])
                .email(creds.getKey())
                .username(creds.getValue())
                .password(passwordEncoder.encode(creds.getValue()))
                .roles(userRoles)
                .questions(questions)
                .isDeleted(false)
                .build();
    }

    private UserRole mapToUserRole(Role role) {

        UserRole userRole = new UserRole();
        userRole.setRoleCon(new RoleCon());
        userRole.getRoleCon().setRole(role);

        return userRole;
    }

    private Question mapToQuestion(long questionId) {

        Question question = new Question();
        question.setId(questionId);

        return question;
    }
}