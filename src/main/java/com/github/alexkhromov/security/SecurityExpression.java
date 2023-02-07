package com.github.alexkhromov.security;

public class SecurityExpression {

    public static final String CREATE_QUESTION_ACL =
            "hasAnyAuthority('USER', 'ADMIN', 'TRANSLATOR', 'MODERATOR')";

    public static final String READ_QUESTION_ACL =
            "@authenticationService.checkQuestionOwnerShip(authentication, #questionId) " +
            "or hasAnyAuthority('ADMIN', 'TRANSLATOR', 'MODERATOR')";

    public static final String UPDATE_QUESTION_ACL =
            "@authenticationService.checkQuestionOwnerShip(authentication, #questionId) " +
            "or hasAnyAuthority('ADMIN', 'TRANSLATOR')";

    public static final String DELETE_QUESTION_ACL =
            "@authenticationService.checkQuestionOwnerShip(authentication, #questionId) " +
            "or hasAnyAuthority('ADMIN', 'MODERATOR')";

    public static final String USER_ACL =
            "@authenticationService.checkUserOwnerShip(authentication, #userId) " +
                    "or hasAnyAuthority('ADMIN')";
}