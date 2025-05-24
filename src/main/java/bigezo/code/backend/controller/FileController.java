package bigezo.code.backend.controller;

import bigezo.code.backend.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Test file upload endpoint
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Using "test" as the file type for testing
            String url = fileService.uploadFile(file, "test");
            return ResponseEntity.ok().body("File uploaded successfully! URL: " + url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading file: " + e.getMessage());
        }
    }

    /**
     * Test file deletion endpoint
     */
    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            fileService.deleteFile(fileName);
            return ResponseEntity.ok().body("File deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting file: " + e.getMessage());
        }
    }

    /**
     * Test endpoint to get a list of all files in the bucket
     */
    @GetMapping("/list")
    public ResponseEntity<String> listFiles() {
        // This endpoint is just for testing - in production you would implement actual listing
        return ResponseEntity.ok().body("File listing endpoint - not implemented yet");
    }
}
