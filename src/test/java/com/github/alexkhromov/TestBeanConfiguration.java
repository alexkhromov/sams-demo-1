package com.github.alexkhromov;

import com.github.alexkhromov.repository.LevelConRepository;
import com.github.alexkhromov.repository.QuestionRepository;
import com.github.alexkhromov.repository.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestBeanConfiguration {

    @Bean
    @Primary
    public QuestionRepository mockQuestionRepository() {
        return mock(QuestionRepository.class);
    }

    @Bean
    @Primary
    public LevelConRepository mockLevelConRepository() {
        return mock(LevelConRepository.class);
    }

    @Bean
    @Primary
    public UserRepository mockUserRepository() {
        return mock(UserRepository.class);
    }
}