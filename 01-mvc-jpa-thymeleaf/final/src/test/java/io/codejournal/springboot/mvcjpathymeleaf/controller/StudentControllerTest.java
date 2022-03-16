package io.codejournal.springboot.mvcjpathymeleaf.controller;

import static io.codejournal.springboot.mvcjpathymeleaf.controller.StudentController.PAGE_SIZE;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.codejournal.springboot.mvcjpathymeleaf.controller.StudentController;
import io.codejournal.springboot.mvcjpathymeleaf.entity.Student;
import io.codejournal.springboot.mvcjpathymeleaf.service.StudentService;

@ExtendWith(SpringExtension.class)
public class StudentControllerTest {

    private MockMvc mvc;

    @MockBean
    private StudentService service;

    private StudentController fixture;

    @BeforeEach
    public void setUp() {

        fixture = new StudentController(service);

        this.mvc = MockMvcBuilders.standaloneSetup(fixture).build();
    }

    @Test
    public void index_RedirectsToListView_WhenStudentHomeIsAccessed() throws Exception {

        // @formatter:off
        mvc.perform(
                get("/students/")
           )
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("list"))
        ;
        // @formatter:on

        then(service).shouldHaveNoInteractions();
    }

    @Test
    public void list_ReturnsViewWithRecords_WhenStudentListViewIsAccessed() throws Exception {

        final int pageNumber = 0;
        final int pageSize = PAGE_SIZE;
        final int totalPages = (int) (Math.random() * 100);

        final Student student1 = new Student(randomUUID(), randomUUID().toString(), randomUUID().toString());
        final Student student2 = new Student(randomUUID(), randomUUID().toString(), randomUUID().toString());

        final List<Student> students = Arrays.asList(student1, student2);
        final Pageable page = PageRequest.of(pageNumber, pageSize);

        final Page<Student> response = new PageImpl<>(students, page, totalPages);

        given(service.getStudents(pageNumber, pageSize)).willReturn(response);

        // @formatter:off
        mvc.perform(
                get("/students/list")
                    .param("page", String.valueOf(pageNumber))
                    .param("size", String.valueOf(pageSize))
           )
           .andExpect(status().isOk())
           .andExpect(model().attribute("students", hasItems(student1, student2)))
           .andExpect(view().name("students/list"))
        ;
        // @formatter:on

        then(service).should().getStudents(pageNumber, pageSize);
        then(service).shouldHaveNoMoreInteractions();
    }

    @Test
    public void list_ReturnsViewForFirstPage_WhenStudentListViewIsAccessed() throws Exception {

        final int pageNumber = 0;
        final int pageSize = PAGE_SIZE;
        final int totalPages = (int) (Math.random() * 100);

        final Student student = new Student(randomUUID(), randomUUID().toString(), randomUUID().toString());
        final Pageable page = PageRequest.of(pageNumber, pageSize);

        final Page<Student> response = new PageImpl<>(singletonList(student), page, totalPages);

        given(service.getStudents(pageNumber, pageSize)).willReturn(response);

        // @formatter:off
        mvc.perform(
                get("/students/list")
                    .param("page", String.valueOf(pageNumber))
                    .param("size", String.valueOf(pageSize))
           )
           .andExpect(status().isOk())
           .andExpect(model().attribute("previous", is(-1)))
           .andExpect(model().attribute("next", is(1)))
           .andExpect(view().name("students/list"))
        ;
        // @formatter:on

        then(service).should().getStudents(pageNumber, pageSize);
        then(service).shouldHaveNoMoreInteractions();
    }

    @Test
    public void list_ReturnsViewForLastPage_WhenStudentListViewIsAccessed() throws Exception {

        final int pageNumber = 1;
        final int pageSize = PAGE_SIZE;
        final int totalPages = PAGE_SIZE;

        final Student student = new Student(randomUUID(), randomUUID().toString(), randomUUID().toString());
        final Pageable page = PageRequest.of(pageNumber, pageSize);

        final Page<Student> response = new PageImpl<>(singletonList(student), page, totalPages);

        given(service.getStudents(pageNumber, pageSize)).willReturn(response);

        // @formatter:off
        mvc.perform(
                get("/students/list")
                    .param("page", String.valueOf(pageNumber))
                    .param("size", String.valueOf(pageSize))
           )
           .andExpect(status().isOk())
           .andExpect(model().attribute("previous", is(0)))
           .andExpect(model().attribute("next", is(-1)))
           .andExpect(view().name("students/list"))
        ;
        // @formatter:on

        then(service).should().getStudents(pageNumber, pageSize);
        then(service).shouldHaveNoMoreInteractions();
    }

    @Test
    public void add_ReturnsAddView_WhenStudentAddViewIsAccessed() throws Exception {

        // @formatter:off
        mvc.perform(
                get("/students/add")
           )
           .andExpect(status().isOk())
           .andExpect(model().attribute("student.id", is(nullValue())))
           .andExpect(model().attribute("student.firstName", is(nullValue())))
           .andExpect(model().attribute("student.lastName", is(nullValue())))
           .andExpect(view().name("students/add"))
        ;
        // @formatter:on

        then(service).shouldHaveNoInteractions();
    }

    @Test
    public void edit_ReturnsEditView_WhenStudentEditViewIsAccessedAndStudentExists() throws Exception {

        final UUID id = randomUUID();

        final Student student = new Student(id, randomUUID().toString(), randomUUID().toString());

        given(service.getStudent(id)).willReturn(Optional.of(student));

        // @formatter:off
        mvc.perform(
                get("/students/edit")
                    .param("id", id.toString())
           )
           .andExpect(status().isOk())
           .andExpect(model().attribute("student", hasProperty("id", is(id))))
           .andExpect(model().attribute("student", hasProperty("firstName", is(student.getFirstName()))))
           .andExpect(model().attribute("student", hasProperty("lastName", is(student.getLastName()))))
           .andExpect(model().attribute("id", is(id)))
           .andExpect(view().name("students/edit"))
        ;
        // @formatter:on

        then(service).should().getStudent(id);
        then(service).shouldHaveNoMoreInteractions();
    }

    @Test
    public void edit_ReturnsEditView_WhenStudentEditViewIsAccessedAndStudentDoesNotExists() throws Exception {

        final UUID id = randomUUID();

        given(service.getStudent(id)).willReturn(Optional.empty());

        // @formatter:off
        mvc.perform(
                get("/students/edit")
                    .param("id", id.toString())
           )
           .andExpect(status().isOk())
           .andExpect(model().attribute("student", hasProperty("id", is(nullValue()))))
           .andExpect(model().attribute("student", hasProperty("firstName", is(nullValue()))))
           .andExpect(model().attribute("student", hasProperty("lastName", is(nullValue()))))
           .andExpect(model().attribute("id", is(id)))
           .andExpect(view().name("students/edit"))
        ;
        // @formatter:on

        then(service).should().getStudent(id);
        then(service).shouldHaveNoMoreInteractions();
    }

    @Test
    public void save_SavesStudentRecord_WhenStudentRecordIsValid() throws Exception {

        final Student student = new Student(randomUUID(), randomUUID().toString(), randomUUID().toString());

        given(service.save(student)).willReturn(student);

        // @formatter:off
        mvc.perform(
                post("/students/save")
                    .flashAttr("student", student)
           )
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("list"))
        ;
        // @formatter:on

        then(service).should().save(student);
        then(service).shouldHaveNoMoreInteractions();
    }

    @Test
    public void delete_ReturnsDeleteView_WhenStudentDeleteViewIsAccessedAndStudentExists() throws Exception {

        final UUID id = randomUUID();

        final Student student = new Student(id, randomUUID().toString(), randomUUID().toString());

        given(service.getStudent(id)).willReturn(Optional.of(student));

        // @formatter:off
        mvc.perform(
                get("/students/delete")
                    .param("id", id.toString())
           )
           .andExpect(status().isOk())
           .andExpect(model().attribute("student", hasProperty("id", is(id))))
           .andExpect(model().attribute("student", hasProperty("firstName", is(student.getFirstName()))))
           .andExpect(model().attribute("student", hasProperty("lastName", is(student.getLastName()))))
           .andExpect(model().attribute("id", is(id)))
           .andExpect(view().name("students/delete"))
        ;
        // @formatter:on

        then(service).should().getStudent(id);
        then(service).shouldHaveNoMoreInteractions();
    }

    @Test
    public void delete_ReturnsDeleteView_WhenStudentDeleteViewIsAccessedAndStudentDoesNotExists() throws Exception {

        final UUID id = randomUUID();

        given(service.getStudent(id)).willReturn(Optional.empty());

        // @formatter:off
        mvc.perform(
                get("/students/delete")
                    .param("id", id.toString())
           )
           .andExpect(status().isOk())
           .andExpect(model().attribute("student", hasProperty("id", is(nullValue()))))
           .andExpect(model().attribute("student", hasProperty("firstName", is(nullValue()))))
           .andExpect(model().attribute("student", hasProperty("lastName", is(nullValue()))))
           .andExpect(model().attribute("id", is(id)))
           .andExpect(view().name("students/delete"))
        ;
        // @formatter:on

        then(service).should().getStudent(id);
        then(service).shouldHaveNoMoreInteractions();
    }

    @Test
    public void deletion_DeletesStudentRecord_WhenStudentRecordIsValid() throws Exception {

        final UUID id = randomUUID();

        willDoNothing().given(service).delete(id);

        // @formatter:off
        mvc.perform(
                post("/students/delete")
                    .param("id", id.toString())
           )
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("list"))
        ;
        // @formatter:on

        then(service).should().delete(id);
        then(service).shouldHaveNoMoreInteractions();
    }
}
