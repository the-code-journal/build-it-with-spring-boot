package io.codejournal.springprojects.mvcjpathymeleaf.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import io.codejournal.springprojects.mvcjpathymeleaf.entity.Student;
import io.codejournal.springprojects.mvcjpathymeleaf.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository repository;

    @Autowired
    public StudentService(final StudentRepository repository) {
        this.repository = repository;
    }

    public Page<Student> getStudents(final int pageNumber, final int pageSize) {
        return repository.findAll(PageRequest.of(pageNumber, pageSize));
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
