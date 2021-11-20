package dev.zentari.fileservice;

import dev.zentari.fileservice.services.StorageProperties;
import dev.zentari.fileservice.services.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class FileServiceApplication {

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return args -> {
            storageService.init();
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(FileServiceApplication.class, args);
    }

}
