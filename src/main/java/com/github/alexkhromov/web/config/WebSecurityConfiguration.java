package com.github.alexkhromov.web.config;

import com.github.alexkhromov.security.JwtTokenProvider;
import com.github.alexkhromov.service.IAuthenticationService;
import com.github.alexkhromov.web.filter.AuthenticationFilter;
import com.github.alexkhromov.web.filter.SkipUriFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.BeanIds.AUTHENTICATION_MANAGER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private IAuthenticationService authenticationService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SkipUriFilter skipUriFilter;

    @Autowired
    private MessageSource messageSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(
                authenticationService, jwtTokenProvider, skipUriFilter, messageSource);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(authenticationService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .cors()
            .and()
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers(GET, "/api/v1/questions").permitAll()
                    .antMatchers("/", "/frontend/**", "/resources/i18n/**", "/signup", "/signin").permitAll()
                    .antMatchers("/api/v1/**").authenticated()
                    .anyRequest().authenticated()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS);

        http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}