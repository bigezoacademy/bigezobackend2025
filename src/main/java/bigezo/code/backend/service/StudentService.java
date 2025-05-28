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
import java.util.Optional;
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

    public Optional<Student> findByIdAndSchoolAdminId(Long studentId, Long schoolAdminId) {
        return studentRepository.findByIdAndSchoolAdminId(studentId, schoolAdminId);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
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
        SchoolAdmin schoolAdmin = schoolAdminRepository.findById(schoolAdminId)
                .orElseThrow(() -> new IllegalArgumentException("SchoolAdmin not found with ID: " + schoolAdminId));

        student.setSchoolAdmin(schoolAdmin);

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(hashedPassword);

        Student savedStudent = studentRepository.save(student);
        return convertToDto(savedStudent);
    }

    public StudentDto updateStudent(Long schoolAdminId, Long id, Student updatedStudent) {
        Student existingStudent = findByIdAndSchoolAdminId(id, schoolAdminId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + id));

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
        existingStudent.setGender(updatedStudent.getGender());

        if (updatedStudent.getPassword() != null && !updatedStudent.getPassword().trim().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(updatedStudent.getPassword());
            existingStudent.setPassword(hashedPassword);
        }

        Student savedStudent = studentRepository.save(existingStudent);
        return convertToDto(savedStudent);
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new IllegalArgumentException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    private StudentDto convertToDto(Student student) {
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
                student.getEnrollmentStatus(),
                student.getYear(),
                student.getGender(),
                student.getSchoolAdmin() != null ? student.getSchoolAdmin().getId() : null,
                student.getProfilePictureUrl(),
                student.getStudentVideoUrl(),
                student.getImage1Url(),
                student.getImage2Url(),
                student.getImage3Url(),
                student.getImage4Url(),
                student.getImage5Url(),
                student.getImage6Url(),
                student.getImage7Url(),
                student.getImage8Url(),
                student.getImage9Url(),
                student.getImage10Url()
        );
    }
}