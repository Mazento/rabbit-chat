package dev.zentari.fileservice;

import dev.zentari.fileservice.services.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

@Slf4j
@Controller
public class FileManagementController {

    private final StorageService storageService;

    public FileManagementController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file,
                                     @RequestParam("fileId") Integer fileId) {

        log.debug("New file upload with id " + fileId);
        storageService.store(file, fileId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, @RequestParam String original) {

        log.debug("Downloading file: " + original);
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + original + "\"").body(file);
    }
}
