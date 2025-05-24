package bigezo.code.backend.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    private final AmazonS3 amazonS3Client;
    private final String bucketName;
    private final String endpoint;

    @Autowired
    public FileService(AmazonS3 amazonS3Client, 
                       @Value("${application.bucket.name}") String bucketName,
                       @Value("${cloud.aws.endpoint.static}") String endpoint) {
        this.amazonS3Client = amazonS3Client;
        this.bucketName = bucketName;
        this.endpoint = endpoint;
    }

    /**
     * Upload a file to S3 and return the URL
     */
    public String uploadFile(MultipartFile file, String fileType) throws IOException {
        String fileName = generateFileName(fileType);
        
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        
        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));
        
        return String.format("%s/file/%s/%s", endpoint, bucketName, fileName);
    }

    /**
     * Delete a file from S3
     */
    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(bucketName, fileName);
    }

    /**
     * Generate a unique file name with timestamp
     */
    private String generateFileName(String fileType) {
        return String.format("%s_%s.jpg", fileType, UUID.randomUUID());
    }
}
