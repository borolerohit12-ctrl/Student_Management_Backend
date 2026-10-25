package com.rohit.studentmanagement.controller;

import com.rohit.studentmanagement.dto.StudentRequest;
import com.rohit.studentmanagement.entity.Student;
import com.rohit.studentmanagement.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@Valid @RequestBody StudentRequest request) {
        Student student = new Student(
                request.getName().trim(),
                request.getEmail().trim(),
                request.getBranch().trim(),
                request.getYear());
        Student savedStudent = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found with ID " + id));
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id,
                                 @Valid @RequestBody StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found with ID " + id));

        student.setName(request.getName().trim());
        student.setEmail(request.getEmail().trim());
        student.setBranch(request.getBranch().trim());
        student.setYear(request.getYear());

        return studentRepository.save(student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found with ID " + id));

        studentRepository.delete(student);
        return ResponseEntity.ok(Map.of("message", "Student deleted successfully"));
    }
}
