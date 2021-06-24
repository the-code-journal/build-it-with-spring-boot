package io.codejournal.springboot.mvcjpathymeleaf.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.codejournal.springboot.mvcjpathymeleaf.entity.Student;
import io.codejournal.springboot.mvcjpathymeleaf.service.StudentService;

@Controller
public class StudentController {

    static final int DEFAULT_PAGE_SIZE = 2;

    private final StudentService service;

    @Autowired
    public StudentController(final StudentService service) {
        this.service = service;
    }

    @GetMapping("/students/")
    public String index() {
        return "redirect:list";
    }

    @GetMapping("/students/list")
    public String list(final Model model, @RequestParam(value = "page", defaultValue = "0") final int pageNumber,
            @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE + "") final int pageSize) {

        final Page<Student> page = service.getStudents(pageNumber, pageSize);

        final int currentPageNumber = page.getNumber();
        final int previousPageNumber = page.hasPrevious() ? currentPageNumber - 1 : -1;
        final int nextPageNumber = page.hasNext() ? currentPageNumber + 1 : -1;

        model.addAttribute("students", page.getContent());
        model.addAttribute("previousPageNumber", previousPageNumber);
        model.addAttribute("nextPageNumber", nextPageNumber);
        model.addAttribute("currentPageNumber", currentPageNumber);
        model.addAttribute("pageSize", pageSize);

        return "students/list";
    }

    @GetMapping("/students/view")
    public String view(final Model model, @RequestParam final UUID id) {

        final Optional<Student> record = service.getStudent(id);

        model.addAttribute("student", record.isPresent() ? record.get() : new Student());
        model.addAttribute("id", id);

        return "students/view";
    }

    @GetMapping("/students/add")
    public String add(final Model model) {

        model.addAttribute("student", new Student());

        return "students/add";
    }

    @GetMapping("/students/edit")
    public String edit(final Model model, @RequestParam final UUID id) {

        final Optional<Student> record = service.getStudent(id);

        model.addAttribute("student", record.isPresent() ? record.get() : new Student());
        model.addAttribute("id", id);

        return "students/edit";
    }

    @GetMapping("/students/delete")
    public String delete(final Model model, @RequestParam final UUID id) {

        final Optional<Student> record = service.getStudent(id);

        model.addAttribute("student", record.isPresent() ? record.get() : new Student());
        model.addAttribute("id", id);

        return "students/delete";
    }

    @PostMapping("/students/save")
    public String save(final Model model, @ModelAttribute final Student student, final BindingResult errors) {

        service.save(student);

        return "redirect:list";
    }

    @PostMapping("/students/delete")
    public String save(final Model model, @RequestParam final UUID id) {

        service.delete(id);

        return "redirect:list";
    }
}
