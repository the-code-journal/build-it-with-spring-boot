package io.codejournal.springboot.mvcjpathymeleaf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.codejournal.springboot.mvcjpathymeleaf.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
