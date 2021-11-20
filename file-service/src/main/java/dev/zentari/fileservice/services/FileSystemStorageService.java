package dev.zentari.fileservice.services;

import dev.zentari.common.FileStatus;
import dev.zentari.fileservice.jms.JmsService;
import dev.zentari.fileservice.utils.Helper;
import dev.zentari.fileservice.utils.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final JmsService jmsService;

    public FileSystemStorageService(StorageProperties storageProperties, JmsService jmsService) {

        this.rootLocation = Paths.get(storageProperties.getLocation());
        this.jmsService = jmsService;
    }

    @Override
    public void init() {

        try {
            if (Files.exists(rootLocation)) {
                return;
            }

            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize the storage", e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void store(MultipartFile file, Integer fileId) {

        try {
            if (file.isEmpty() || file.getOriginalFilename() == null) {
                throw new StorageException("Cannot store file: file is empty");
            }

            String filename = Helper.generateFilename(file.getOriginalFilename());

            Path relativePath = this.rootLocation.resolve(filename);
            Path destinationFile  = relativePath.normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside of project's root directory.");
            }

            jmsService.sendMessage(fileId, FileStatus.PROCESSING);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            jmsService.sendMessage(fileId, FileStatus.SUCCESS, filename);

        } catch (IOException | NoSuchAlgorithmException e) {
            jmsService.sendMessage(fileId, FileStatus.FAILURE);
            throw new StorageException("Failed to store file: ", e);
        }
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageException("Failed to read file: " + filename, e);
        }
    }
}
