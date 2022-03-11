package io.codejournal.springprojects.mvcjpathymeleaf.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.codejournal.springprojects.mvcjpathymeleaf.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
}
