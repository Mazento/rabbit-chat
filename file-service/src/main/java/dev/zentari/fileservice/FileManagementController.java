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

    /**
     * Accept and store a file
     * @param file file to be stored
     * @param fileId id that identifies this file in the database
     * @return HTTP/1.1 200 OK
     */
    @PostMapping("/upload")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file,
                                     @RequestParam("fileId") Integer fileId) {

        log.debug("New file upload with id " + fileId);
        storageService.store(file, fileId);
        return ResponseEntity.ok().build();
    }

    /**
     * Download stored file
     * @param filename file to be downloaded
     * @param original original name of the file
     * @return file as HTTP body attachment
     */
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, @RequestParam String original) {

        log.debug("Downloading file: " + original);
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + original + "\"").body(file);
    }
}
