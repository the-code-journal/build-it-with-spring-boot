package io.codejournal.springprojects.mvcjpathymeleaf;

import static java.util.UUID.randomUUID;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import io.codejournal.springprojects.mvcjpathymeleaf.entity.Student;
import io.codejournal.springprojects.mvcjpathymeleaf.repository.StudentRepository;

@SpringBootApplication
@EnableWebSecurity
@EnableWebMvc
public class Application {

    @Autowired
    private StudentRepository studentRepository;

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ApplicationRunner initDefaultStudents() {

        final Student defaultStudent1 = new Student(randomUUID(), "John", "Doe");
        final Student defaultStudent2 = new Student(randomUUID(), "Linda", "Rostam");

        return args -> studentRepository.saveAll(Arrays.asList(defaultStudent1, defaultStudent2));
    }
}
