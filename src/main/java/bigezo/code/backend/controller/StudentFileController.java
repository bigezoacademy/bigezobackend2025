package bigezo.code.backend.controller;

import bigezo.code.backend.model.Student;
import bigezo.code.backend.service.StudentService;
import bigezo.code.backend.service.file.StudentFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/students")
public class StudentFileController {

    private final StudentService studentService;
    private final StudentFileService studentFileService;

    @Autowired
    public StudentFileController(StudentService studentService, StudentFileService studentFileService) {
        this.studentService = studentService;
        this.studentFileService = studentFileService;
    }

    /**
     * Upload profile picture for student
     */
    @PostMapping("/{studentId}/profile-picture")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> uploadProfilePicture(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file) throws Exception {
        Student student = studentService.findByIdAndSchoolAdminId(studentId, schoolAdminId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        // Delete existing profile picture if it exists
        studentFileService.deleteProfilePicture(student);

        // Upload new profile picture
        String url = studentFileService.uploadProfilePicture(file, student);
        student.setProfilePictureUrl(url);
        studentService.save(student);

        return ResponseEntity.ok().body("Profile picture uploaded successfully!");
    }

    /**
     * Upload student video
     */
    @PostMapping("/{studentId}/video")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> uploadStudentVideo(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @PathVariable Long studentId,
            @RequestParam("file") MultipartFile file) throws Exception {
        Student student = studentService.findByIdAndSchoolAdminId(studentId, schoolAdminId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        // Delete existing video if it exists
        studentFileService.deleteStudentVideo(student);

        // Upload new video
        String url = studentFileService.uploadStudentVideo(file, student);
        student.setStudentVideoUrl(url);
        studentService.save(student);

        return ResponseEntity.ok().body("Student video uploaded successfully!");
    }

    /**
     * Upload additional image
     */
    @PostMapping("/{studentId}/image/{imageNumber}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> uploadImage(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @PathVariable Long studentId,
            @PathVariable int imageNumber,
            @RequestParam("file") MultipartFile file) throws Exception {
        if (imageNumber < 1 || imageNumber > 10) {
            return ResponseEntity.badRequest().body("Image number must be between 1 and 10");
        }

        Student student = studentService.findByIdAndSchoolAdminId(studentId, schoolAdminId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        // Delete existing image if it exists
        studentFileService.deleteImage(student, imageNumber);

        // Upload new image
        String url = studentFileService.uploadImage(file, student, imageNumber);
        setStudentImageUrl(student, url, imageNumber);
        studentService.save(student);

        return ResponseEntity.ok().body("Image uploaded successfully!");
    }

    /**
     * Delete profile picture
     */
    @DeleteMapping("/{studentId}/profile-picture")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteProfilePicture(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @PathVariable Long studentId) {
        Student student = studentService.findByIdAndSchoolAdminId(studentId, schoolAdminId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        studentFileService.deleteProfilePicture(student);
        student.setProfilePictureUrl(null);
        studentService.save(student);

        return ResponseEntity.ok().body("Profile picture deleted successfully!");
    }

    /**
     * Delete student video
     */
    @DeleteMapping("/{studentId}/video")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteStudentVideo(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @PathVariable Long studentId) {
        Student student = studentService.findByIdAndSchoolAdminId(studentId, schoolAdminId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        studentFileService.deleteStudentVideo(student);
        student.setStudentVideoUrl(null);
        studentService.save(student);

        return ResponseEntity.ok().body("Student video deleted successfully!");
    }

    /**
     * Delete additional image
     */
    @DeleteMapping("/{studentId}/image/{imageNumber}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteImage(
            @RequestParam(name = "schoolAdminId") Long schoolAdminId,
            @PathVariable Long studentId,
            @PathVariable int imageNumber) {
        if (imageNumber < 1 || imageNumber > 10) {
            return ResponseEntity.badRequest().body("Image number must be between 1 and 10");
        }

        Student student = studentService.findByIdAndSchoolAdminId(studentId, schoolAdminId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + studentId));

        studentFileService.deleteImage(student, imageNumber);
        setStudentImageUrl(student, null, imageNumber);
        studentService.save(student);

        return ResponseEntity.ok().body("Image deleted successfully!");
    }

    private void setStudentImageUrl(Student student, String url, int imageNumber) {
        switch (imageNumber) {
            case 1:
                student.setImage1Url(url);
                break;
            case 2:
                student.setImage2Url(url);
                break;
            case 3:
                student.setImage3Url(url);
                break;
            case 4:
                student.setImage4Url(url);
                break;
            case 5:
                student.setImage5Url(url);
                break;
            case 6:
                student.setImage6Url(url);
                break;
            case 7:
                student.setImage7Url(url);
                break;
            case 8:
                student.setImage8Url(url);
                break;
            case 9:
                student.setImage9Url(url);
                break;
            case 10:
                student.setImage10Url(url);
                break;
            default:
                throw new IllegalArgumentException("Image number must be between 1 and 10");
        }
    }

    private String getImageUrl(Student student, int imageNumber) {
        switch (imageNumber) {
            case 1:
                return student.getImage1Url();
            case 2:
                return student.getImage2Url();
            case 3:
                return student.getImage3Url();
            case 4:
                return student.getImage4Url();
            case 5:
                return student.getImage5Url();
            case 6:
                return student.getImage6Url();
            case 7:
                return student.getImage7Url();
            case 8:
                return student.getImage8Url();
            case 9:
                return student.getImage9Url();
            case 10:
                return student.getImage10Url();
            default:
                return null;
        }
    }
}
