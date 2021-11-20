package dev.zentari.fileservice.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void init();

    void deleteAll();

    void store(MultipartFile file, Integer fileId);

    Resource loadAsResource(String filename);

}
