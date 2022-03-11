package io.codejournal.springprojects.mvcjpathymeleaf.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.codejournal.springprojects.mvcjpathymeleaf.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(final String username);
}
