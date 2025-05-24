package bigezo.code.backend.service.file;

import bigezo.code.backend.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class StudentFileService {

    private final FileService fileService;

    @Autowired
    public StudentFileService(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Upload profile picture for student
     */
    public String uploadProfilePicture(MultipartFile file, Student student) throws IOException {
        return fileService.uploadFile(file, "profile_picture");
    }

    /**
     * Upload student video
     */
    public String uploadStudentVideo(MultipartFile file, Student student) throws IOException {
        return fileService.uploadFile(file, "student_video");
    }

    /**
     * Upload additional image 1
     */
    public String uploadImage1(MultipartFile file, Student student) throws IOException {
        return fileService.uploadFile(file, "image1");
    }

    /**
     * Upload additional image 2
     */
    public String uploadImage2(MultipartFile file, Student student) throws IOException {
        return fileService.uploadFile(file, "image2");
    }

    /**
     * Delete profile picture
     */
    public void deleteProfilePicture(Student student) {
        if (student.getProfilePictureUrl() != null) {
            String fileName = extractFileName(student.getProfilePictureUrl());
            fileService.deleteFile(fileName);
        }
    }

    /**
     * Delete student video
     */
    public void deleteStudentVideo(Student student) {
        if (student.getStudentVideoUrl() != null) {
            String fileName = extractFileName(student.getStudentVideoUrl());
            fileService.deleteFile(fileName);
        }
    }

    /**
     * Delete image 1
     */
    public void deleteImage1(Student student) {
        if (student.getImage1Url() != null) {
            String fileName = extractFileName(student.getImage1Url());
            fileService.deleteFile(fileName);
        }
    }

    /**
     * Delete image 2
     */
    public void deleteImage2(Student student) {
        if (student.getImage2Url() != null) {
            String fileName = extractFileName(student.getImage2Url());
            fileService.deleteFile(fileName);
        }
    }

    /**
     * Extract file name from URL
     */
    private String extractFileName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
