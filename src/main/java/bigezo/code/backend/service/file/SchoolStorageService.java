package bigezo.code.backend.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class SchoolStorageService {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public SchoolStorageService(AmazonS3 amazonS3, @Value("${application.bucket.name}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
        System.out.println("Initialized SchoolStorageService with bucket name: " + bucketName);
    }

    /**
     * Generate a unique file path for a school
     */
    private String generateFilePath(String schoolId, String fileType) {
        return String.format("school-%s/students/%s/%s_%s",
                schoolId,
                fileType,
                fileType,
                UUID.randomUUID().toString());
    }

    /**
     * Upload file for a specific school
     */
    public String uploadFileForSchool(String schoolId, MultipartFile file, String fileType) throws IOException {
        System.out.println("Uploading file to bucket: " + bucketName + ", path: " + generateFilePath(schoolId, fileType));
        // Check if school has active subscription
        if (!checkSchoolSubscription(schoolId)) {
            throw new IllegalStateException("School subscription is not active");
        }

        String filePath = generateFilePath(schoolId, fileType);
        
        // Upload file
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        
        amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata));

        // Return URL that will only work when school is active
        return String.format("%s/%s", bucketName, filePath);
    }

    /**
     * Delete file for a specific school
     */
    public void deleteFileForSchool(String schoolId, String filePath) {
        // Only delete if school has active subscription
        if (checkSchoolSubscription(schoolId)) {
            amazonS3.deleteObject(bucketName, filePath);
        }
    }

    /**
     * Check if school has active subscription
     * This should be implemented based on your subscription system
     */
    private boolean checkSchoolSubscription(String schoolId) {
        // Implement your subscription check logic here
        // For example, check against your database
        return true; // Replace with actual implementation
    }

    /**
     * Get list of files for a specific school
     */
    public ListObjectsV2Result listFilesForSchool(String schoolId) {
        if (!checkSchoolSubscription(schoolId)) {
            return new ListObjectsV2Result(); // Empty result if subscription is not active
        }
        
        return amazonS3.listObjectsV2(new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(String.format("school-%s/", schoolId)));
    }

    /**
     * Get file URL for a specific school
     * Returns null if school subscription is not active
     */
    public String getFileUrl(String schoolId, String filePath) {
        if (!checkSchoolSubscription(schoolId)) {
            return null; // Return null if subscription is not active
        }
        return String.format("https://%s/%s", bucketName, filePath);
    }
}
