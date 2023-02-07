package com.github.alexkhromov.web.config;

import com.github.alexkhromov.web.resolver.PageRequestArgumentResolver;
import com.github.alexkhromov.web.resolver.LevelArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PageRequestArgumentResolver());
        resolvers.add(new LevelArgumentResolver());
    }
}