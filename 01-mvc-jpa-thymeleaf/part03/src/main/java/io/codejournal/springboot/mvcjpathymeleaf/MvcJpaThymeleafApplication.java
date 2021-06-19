package io.codejournal.springboot.mvcjpathymeleaf;

import static java.util.UUID.randomUUID;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.codejournal.springboot.mvcjpathymeleaf.entity.Student;
import io.codejournal.springboot.mvcjpathymeleaf.repository.StudentRepository;

@SpringBootApplication
public class MvcJpaThymeleafApplication {

    @Autowired
    private StudentRepository studentRepository;

    public static void main(final String[] args) {
        SpringApplication.run(MvcJpaThymeleafApplication.class, args);
    }

    @Bean
    public ApplicationRunner initializeStudents() {

        final Student defaultStudent1 = new Student(randomUUID(), "John", "Doe");
        final Student defaultStudent2 = new Student(randomUUID(), "Linda", "Rostam");

        return args -> studentRepository.saveAll(Arrays.asList(defaultStudent1, defaultStudent2));
    }
}
