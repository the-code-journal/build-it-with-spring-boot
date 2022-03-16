package io.codejournal.springboot.mvcjpathymeleaf.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.codejournal.springboot.mvcjpathymeleaf.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
}
