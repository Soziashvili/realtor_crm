package ru.realtorcrm.content_loader_adapter.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.realtorcrm.content_loader_adapter.service.ContentLoaderService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequestMapping("/api/content-loader")
@AllArgsConstructor
public class ContentLoaderController {

    private final ContentLoaderService contentLoaderService;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAndProcessFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("source") String source) {

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            Files.write(filePath, file.getBytes());

            contentLoaderService.processFile(filePath.toString(), source);

            return ResponseEntity.ok("File processed successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing file: " + e.getMessage());
        }
    }

    @PostMapping("/process")
    public ResponseEntity<String> processExistingFile(
            @RequestParam("filePath") String filePath,
            @RequestParam("source") String source) {

        try {
            contentLoaderService.processFile(filePath, source);
            return ResponseEntity.ok("File processed successfully: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error processing file: " + e.getMessage());
        }
    }
}
