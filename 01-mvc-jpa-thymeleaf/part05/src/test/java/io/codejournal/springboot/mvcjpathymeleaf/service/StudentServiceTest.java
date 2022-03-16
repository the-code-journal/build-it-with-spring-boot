package io.codejournal.springboot.mvcjpathymeleaf.service;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.codejournal.springboot.mvcjpathymeleaf.entity.Student;
import io.codejournal.springboot.mvcjpathymeleaf.repository.StudentRepository;

@ExtendWith(SpringExtension.class)
public class StudentServiceTest {

    @MockBean
    private StudentRepository repository;

    private StudentService fixture;

    @BeforeEach
    public void setUp() {
        fixture = new StudentService(repository);
    }

    @Test
    public void getStudents_ReturnsStudents_WhenStudentsExists() {

        final int pageNumber = (int) (Math.random() * 100);
        final int pageSize = (int) (Math.random() * 100);

        final int totalRecords = (int) (Math.random() * 100);

        final Student student1 = new Student(randomUUID(), randomUUID().toString(), randomUUID().toString());
        final Student student2 = new Student(randomUUID(), randomUUID().toString(), randomUUID().toString());
        final Student student3 = new Student(randomUUID(), randomUUID().toString(), randomUUID().toString());

        final List<Student> students = Arrays.asList(student1, student2, student3);

        final PageRequest page = PageRequest.of(pageNumber, pageSize);

        final Page<Student> expected = new PageImpl<>(students, page, totalRecords);

        given(repository.findAll(page)).willReturn(expected);

        final Page<Student> actual = fixture.getStudents(pageNumber, pageSize);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getContent()).hasSameElementsAs(students);
        assertThat(actual.getPageable()).isEqualTo(page);

        then(repository).should().findAll(page);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    public void getStudent_ReturnsStudent_WhenStudentExist() {

        final UUID id = randomUUID();

        final Student student = new Student(randomUUID(), randomUUID().toString(), randomUUID().toString());

        final Optional<Student> expected = Optional.of(student);

        given(repository.findById(id)).willReturn(expected);

        final Optional<Student> actual = fixture.getStudent(id);

        assertThat(actual).isEqualTo(expected);

        then(repository).should().findById(id);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    public void getStudent_ReturnsStudent_WhenStudentDoesNotExist() {

        final UUID id = randomUUID();

        final Optional<Student> expected = Optional.empty();

        given(repository.findById(id)).willReturn(expected);

        final Optional<Student> actual = fixture.getStudent(id);

        assertThat(actual).isEqualTo(expected);

        then(repository).should().findById(id);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    public void save_ReturnSaved_WhenStudentRecordIsCreated() {

        final UUID id = randomUUID();

        final Student expected = new Student();
        expected.setFirstName(randomUUID().toString());
        expected.setLastName(randomUUID().toString());

        given(repository.save(expected)).willAnswer(invocation -> {

            final Student toSave = invocation.getArgument(0);

            toSave.setId(id);

            return toSave;
        });

        final Student actual = fixture.save(expected);

        assertThat(actual).isEqualTo(expected);

        then(repository).should().save(expected);
        then(repository).shouldHaveNoMoreInteractions();
    }

    @Test
    public void delete_DeletesStudent_WhenStudentExists() {

        final UUID id = randomUUID();

        willDoNothing().given(repository).deleteById(id);

        fixture.delete(id);

        then(repository).should().deleteById(id);
        then(repository).shouldHaveNoMoreInteractions();
    }
}
