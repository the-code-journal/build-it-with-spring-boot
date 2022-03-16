package io.codejournal.springboot.mvcjpathymeleaf.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import io.codejournal.springboot.mvcjpathymeleaf.entity.Student;
import io.codejournal.springboot.mvcjpathymeleaf.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository repository;

    @Autowired
    public StudentService(final StudentRepository repository) {
        this.repository = repository;
    }

    public Page<Student> getStudents(final int pageNumber, final int size) {
        return repository.findAll(PageRequest.of(pageNumber, size));
    }

    public Optional<Student> getStudent(final UUID id) {
        return repository.findById(id);
    }

    public Student save(final Student student) {
        return repository.save(student);
    }

    public void delete(final UUID id) {
        repository.deleteById(id);
    }
}
