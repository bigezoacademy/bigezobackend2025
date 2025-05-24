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
     * Upload additional image
     */
    public String uploadImage(MultipartFile file, Student student, int imageNumber) throws IOException {
        if (imageNumber < 1 || imageNumber > 10) {
            throw new IllegalArgumentException("Image number must be between 1 and 10");
        }
        return fileService.uploadFile(file, "image" + imageNumber);
    }

    /**
     * Delete profile picture
     */
    public void deleteProfilePicture(Student student) {
        deleteFile(student.getProfilePictureUrl());
    }

    /**
     * Delete student video
     */
    public void deleteStudentVideo(Student student) {
        deleteFile(student.getStudentVideoUrl());
    }

    /**
     * Delete image
     */
    public void deleteImage(Student student, int imageNumber) {
        String url = getImageUrl(student, imageNumber);
        if (url != null) {
            deleteFile(url);
        }
    }

    /**
     * Delete file by URL
     */
    private void deleteFile(String url) {
        if (url != null) {
            String fileName = extractFileName(url);
            fileService.deleteFile(fileName);
        }
    }

    /**
     * Get image URL by number
     */
    private String getImageUrl(Student student, int imageNumber) {
        switch (imageNumber) {
            case 1: return student.getImage1Url();
            case 2: return student.getImage2Url();
            case 3: return student.getImage3Url();
            case 4: return student.getImage4Url();
            case 5: return student.getImage5Url();
            case 6: return student.getImage6Url();
            case 7: return student.getImage7Url();
            case 8: return student.getImage8Url();
            case 9: return student.getImage9Url();
            case 10: return student.getImage10Url();
            default: return null;
        }
    }

    /**
     * Extract file name from URL
     */
    private String extractFileName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
