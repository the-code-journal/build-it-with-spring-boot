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
public class UserServiceTest {

    @MockBean
    private UserRepository repository;

    private UserService fixture;

    @BeforeEach
    public void setUp() {
        fixture = new UserService(repository);
    }

    @Test
    public void loadUserByUsername_ReturnUser_WhenUserExists() {

        final String username = randomUUID().toString();
        final String password = randomUUID().toString();

        final User user = new User(username, password);

        // @formatter:off
        final UserDetails expected = new org.springframework.security.core.userdetails.User(
                                            username,
                                            password,
                                            singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                                     );
        // @formatter:on

        given(repository.findByUsername(username)).willReturn(Optional.of(user));

        final UserDetails actual = fixture.loadUserByUsername(username);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

        then(repository).should().findByUsername(username);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    public void loadUserByUsername_ReturnUser_WhenUserDoesNotExists() {

        final String username = randomUUID().toString();

        given(repository.findByUsername(username)).willReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> fixture.loadUserByUsername(username))
                .withMessageContaining(username);

        then(repository).should().findByUsername(username);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    public void save_ReturnSaved_WhenUserRecordIsCreated() {

        final User expected = new User();

        expected.setUsername(randomUUID().toString());
        expected.setPassword(randomUUID().toString());

        given(repository.save(expected)).willReturn(expected);

        final User actual = fixture.save(expected);

        assertThat(actual).isEqualTo(expected);

        then(repository).should().save(expected);
        then(repository).shouldHaveNoMoreInteractions();
    }
}
