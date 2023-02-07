package com.github.alexkhromov.model.entity.listener;

import com.github.alexkhromov.common.ApplicationConstant;
import com.github.alexkhromov.model.entity.User;
import com.github.alexkhromov.model.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PreRemove;

import static com.github.alexkhromov.model.error.exception.SamsDemoException.entityNotFoundException;
import static java.lang.String.format;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Slf4j
@Component
public class UserListener implements ApplicationContextAware {

    private static ApplicationContext context;

    private static final String READ_ANONYMOUS_USER_QUERY =
            String.format("FROM com.github.alexkhromov.model.entity.User U WHERE U.id = %s", ApplicationConstant.ANONYMOUS_USER_ID);

    @PreRemove
    @Transactional(propagation = MANDATORY)
    public void resetUserQuestions(User user) {

        if (user.getIsDeleted()) {

            log.error("Entity not found exception [resetUserQuestions]: {}, {}",
                    User.class.getSimpleName(), user.getId().toString());

            throw entityNotFoundException(
                    ErrorCode.ENTITY_NOT_FOUND,
                    User.class.getSimpleName(),
                    user.getId().toString());
        }

        EntityManager entityManager = getEntityManager();

        User anonymous = entityManager
                .createQuery(READ_ANONYMOUS_USER_QUERY, User.class)
                .getSingleResult();

        emptyIfNull(user.getQuestions()).forEach(q -> q.setUser(anonymous));
        user.setQuestions(null);

        entityManager.persist(anonymous);
        entityManager.persist(user);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    private EntityManager getEntityManager() {
        return context.getBean(EntityManager.class);
    }
}