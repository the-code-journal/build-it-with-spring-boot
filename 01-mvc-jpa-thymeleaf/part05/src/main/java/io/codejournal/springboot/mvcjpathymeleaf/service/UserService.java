package io.codejournal.springboot.mvcjpathymeleaf.service;

import static java.util.Collections.singletonList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.codejournal.springboot.mvcjpathymeleaf.entity.User;
import io.codejournal.springboot.mvcjpathymeleaf.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;

    @Autowired
    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        final Optional<User> record = repository.findByUsername(username);

        if (record.isEmpty()) {
            throw new UsernameNotFoundException("User not found - " + username);
        }

        final User user = record.get();

        // @formatter:off
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        // @formatter:on
    }

    public User save(final User user) {
        return repository.save(user);
    }
}
