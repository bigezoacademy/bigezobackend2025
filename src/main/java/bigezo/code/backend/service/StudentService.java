package bigezo.code.backend.service;

import bigezo.code.backend.SchoolAdmin;
import bigezo.code.backend.SchoolAdminDto;
import bigezo.code.backend.SchoolAdminRepository;
import bigezo.code.backend.model.Student;
import bigezo.code.backend.model.StudentDto;
import bigezo.code.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolAdminRepository schoolAdminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          SchoolAdminRepository schoolAdminRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.schoolAdminRepository = schoolAdminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<StudentDto> getAllStudents(Long schoolAdminId) {
        return studentRepository.findBySchoolAdminId(schoolAdminId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<StudentDto> getStudentsByFilters(Long schoolAdminId, int year, String level, String enrollmentStatus) {
        List<Student> students = studentRepository.findBySchoolAdminIdAndYearAndLevelAndEnrollmentStatus(schoolAdminId, year, level, enrollmentStatus);
        return students.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public StudentDto createStudent(Long schoolAdminId, Student student) {
        // Fetch the SchoolAdmin entity by schoolAdminId
        SchoolAdmin schoolAdmin = schoolAdminRepository.findById(schoolAdminId)
                .orElseThrow(() -> new IllegalArgumentException("SchoolAdmin not found with id: " + schoolAdminId));

        // Set the SchoolAdmin entity in the Student
        student.setSchoolAdmin(schoolAdmin);

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(hashedPassword);

        // Save the student entity
        Student savedStudent = studentRepository.save(student);
        return convertToDto(savedStudent);
    }

    public StudentDto updateStudent(Long schoolAdminId, Long id, Student updatedStudent) {
        // Find the existing student by ID and SchoolAdmin ID
        Student existingStudent = studentRepository.findByIdAndSchoolAdminId(id, schoolAdminId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        // Update fields
        existingStudent.setFirstName(updatedStudent.getFirstName());
        existingStudent.setLastName(updatedStudent.getLastName());
        existingStudent.setLevel(updatedStudent.getLevel());
        existingStudent.setClub(updatedStudent.getClub());
        existingStudent.setHealthStatus(updatedStudent.getHealthStatus());
        existingStudent.setStudentNumber(updatedStudent.getStudentNumber());
        existingStudent.setBirthDate(updatedStudent.getBirthDate());
        existingStudent.setResidence(updatedStudent.getResidence());
        existingStudent.setMother(updatedStudent.getMother());
        existingStudent.setFather(updatedStudent.getFather());
        existingStudent.setPhone(updatedStudent.getPhone());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setEnrollmentStatus(updatedStudent.getEnrollmentStatus());
        existingStudent.setYear(updatedStudent.getYear());

        // Hash the password if it is being updated
        if (updatedStudent.getPassword() != null && !updatedStudent.getPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(updatedStudent.getPassword());
            existingStudent.setPassword(hashedPassword);
        }

        // Save the updated student entity
        Student savedStudent = studentRepository.save(existingStudent);
        return convertToDto(savedStudent);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    private StudentDto convertToDto(Student student) {
        SchoolAdminDto schoolAdminDto = new SchoolAdminDto(
                student.getSchoolAdmin().getId(),
                student.getSchoolAdmin().getSchoolName(),
                student.getSchoolAdmin().getDistrict()
        );

        return new StudentDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getLevel(),
                student.getClub(),
                student.getHealthStatus(),
                student.getStudentNumber(),
                student.getBirthDate(),
                student.getResidence(),
                student.getMother(),
                student.getFather(),
                student.getPhone(),
                student.getEmail(),
                student.getPassword(), // You may want to omit the password from the DTO in production
                student.getEnrollmentStatus(),
                student.getYear(),
                student.getSchoolAdmin() != null ? student.getSchoolAdmin().getId() : null
        );
    }
}
