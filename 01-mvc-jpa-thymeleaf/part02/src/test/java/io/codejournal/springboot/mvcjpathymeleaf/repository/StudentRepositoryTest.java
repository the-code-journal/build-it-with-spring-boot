package io.codejournal.springboot.mvcjpathymeleaf.repository;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.codejournal.springboot.mvcjpathymeleaf.entity.Student;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository repository;

    @Test
    public void save_StoresRecord_WhenRecordIsvalid() {

        final Student expected = new Student();
        expected.setFirstName(randomUUID().toString());
        expected.setLastName(randomUUID().toString());

        final Student saved = repository.save(expected);

        final Student actual = entityManager.find(Student.class, saved.getId());

        assertThat(actual).isEqualTo(expected);
    }
}
