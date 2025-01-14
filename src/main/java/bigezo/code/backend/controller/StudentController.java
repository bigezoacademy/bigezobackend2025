package bigezo.code.backend.controller;

import bigezo.code.backend.model.*;
import bigezo.code.backend.model.Student;
import bigezo.code.backend.model.StudentDto;
import bigezo.code.backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{schoolAdminId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<StudentDto>> getAllStudents(@PathVariable Long schoolAdminId) {
        return ResponseEntity.ok(studentService.getAllStudents(schoolAdminId));
    }


    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<StudentDto>> getAllEnrolledStudents(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "level", required = false) String level,
            @RequestParam(name = "enrollmentStatus", required = false) String enrollmentStatus) {
        List<StudentDto> students;
        if (year != null && level != null && enrollmentStatus != null) {
            students = studentService.getStudentsByFilters(schoolAdminId, year, level, enrollmentStatus);
        } else {
            students = studentService.getAllStudents(schoolAdminId);
        }
        return ResponseEntity.ok(students);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentDto> createStudent(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @RequestBody Student student) {
        StudentDto createdStudent = studentService.createStudent(schoolAdminId, student);
        return ResponseEntity.ok(createdStudent);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentDto> updateStudent(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @PathVariable Long id,
            @RequestBody Student student) {
        StudentDto updatedStudent = studentService.updateStudent(schoolAdminId, id, student);
        return ResponseEntity.ok(updatedStudent);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
