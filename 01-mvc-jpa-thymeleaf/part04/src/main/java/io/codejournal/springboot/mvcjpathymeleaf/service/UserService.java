package io.codejournal.springboot.mvcjpathymeleaf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.codejournal.springboot.mvcjpathymeleaf.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(final UserRepository repository) {
        this.repository = repository;
    }
}
