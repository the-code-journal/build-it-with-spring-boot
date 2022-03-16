package io.codejournal.springboot.mvcjpathymeleaf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.codejournal.springboot.mvcjpathymeleaf.entity.User;
import io.codejournal.springboot.mvcjpathymeleaf.service.UserService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String DEFAULT_USER = "code-journal";

    public static final String DEFAULT_ROLE = "USER";

    @Autowired
    private UserService service;

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http.formLogin()
                .defaultSuccessUrl("/students/")
            .and()
                .logout()
                .permitAll()
            .and()
                .authorizeRequests()
                .antMatchers("/**").hasRole("USER")
        ;
        // @formatter:on
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(service);

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner initUsers() {
        return args -> service
                .save(new User(DEFAULT_USER, passwordEncoder().encode("password"), "ROLE_" + DEFAULT_ROLE));
    }
}
