package io.codejournal.springprojects.mvcjpathymeleaf.controller;

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

import io.codejournal.springprojects.mvcjpathymeleaf.entity.Student;
import io.codejournal.springprojects.mvcjpathymeleaf.service.StudentService;

@Controller
public class StudentController {

    static final int PAGE_SIZE = 2;

    private final StudentService service;

    @Autowired
    public StudentController(final StudentService service) {
        this.service = service;
    }

    @GetMapping("/students/")
    public String index(final Model model) {
        return "redirect:list";
    }

    @GetMapping("/students/list")
    public String list(final Model model,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int pageNumber,
            @RequestParam(name = "size", required = false, defaultValue = PAGE_SIZE + "") final int pageSize) {

        final Page<Student> page = service.getStudents(pageNumber, pageSize);

        final int currentPage = page.getNumber();
        final int previous = page.hasPrevious() ? currentPage - 1 : -1;
        final int next = page.hasNext() ? currentPage + 1 : -1;

        model.addAttribute("previous", previous);
        model.addAttribute("next", next);
        model.addAttribute("current", currentPage);
        model.addAttribute("size", pageSize);

        model.addAttribute("students", page.getContent());

        return "students/list";
    }

    @GetMapping("/students/view")
    public String view(final Model model, @RequestParam final UUID id) {

        final Optional<Student> student = service.getStudent(id);

        model.addAttribute("student", (student.isPresent()) ? student.get() : new Student());
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

        final Optional<Student> student = service.getStudent(id);

        model.addAttribute("student", (student.isPresent()) ? student.get() : new Student());
        model.addAttribute("id", id);

        return "students/edit";
    }

    @PostMapping("/students/save")
    public String edit(@ModelAttribute final Student student, final BindingResult errors, final Model model) {

        service.save(student);

        return "redirect:list";
    }

    @GetMapping("/students/delete")
    public String delete(final Model model, @RequestParam final UUID id) {

        final Optional<Student> student = service.getStudent(id);

        model.addAttribute("student", (student.isPresent()) ? student.get() : new Student());
        model.addAttribute("id", id);

        return "students/delete";
    }

    @PostMapping("/students/delete")
    public String deletion(final Model model, @RequestParam final UUID id) {

        service.delete(id);

        return "redirect:list";
    }
}
