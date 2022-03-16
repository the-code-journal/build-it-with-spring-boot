package io.codejournal.springboot.mvcjpathymeleaf.service;

import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.codejournal.springboot.mvcjpathymeleaf.entity.User;
import io.codejournal.springboot.mvcjpathymeleaf.repository.UserRepository;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @MockBean
    private UserRepository repository;

    private UserService fixture;

    @BeforeEach
    public void setUp() {
        fixture = new UserService(repository);
    }

    @Test
    public void loadUserByUsername_ReturnsUser_WhenUserExists() {

        final String username = randomUUID().toString();
        final String password = randomUUID().toString();
        final String role = randomUUID().toString();

        // @formatter:off
        final UserDetails expected = new org.springframework.security.core.userdetails.User(
                                                username,
                                                password,
                                                singletonList(new SimpleGrantedAuthority(role))
                                     );
        // @formatter:on

        given(repository.findByUsername(username)).willReturn(Optional.of(new User(username, password, role)));

        final UserDetails actual = fixture.loadUserByUsername(username);

        assertThat(actual).isEqualTo(expected);

        then(repository).should().findByUsername(username);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    public void loadUserByUsername_ThrowsUsernameNotFoundException_WhenUserDoesNotExists() {

        final String username = randomUUID().toString();

        given(repository.findByUsername(username)).willReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> fixture.loadUserByUsername(username));

        then(repository).should().findByUsername(username);
        then(repository).shouldHaveNoMoreInteractions();
    }
}
