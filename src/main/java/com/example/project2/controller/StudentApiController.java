package com.example.project2.controller;

import com.example.project2.model.StudentModel;
import com.example.project2.service.StudentService;
import com.example.project2.service.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/student")
public class StudentApiController {

    private final StudentService studentService;

    public StudentApiController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentModel> getAllStudents() {
        return studentService.findAllStudent();
    }

    @GetMapping("/{id}")
    public StudentModel getStudentById(@PathVariable Long id) {
        return studentService.findStudentById(id);
    }

    @PostMapping
    public StudentModel createStudent(@RequestBody StudentModel student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    public StudentModel updateStudent(@PathVariable Long id, @RequestBody StudentModel student) {
        student.setId(id);
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
