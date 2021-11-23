package dev.zentari.chatsignalservice.services;

import dev.zentari.chatsignalservice.models.FileRecord;
import dev.zentari.chatsignalservice.repositories.FileRecordRepository;
import org.springframework.stereotype.Service;

@Service
public class FileRecordServiceImpl implements FileRecordService {

    FileRecordRepository fileRecordRepository;

    public FileRecordServiceImpl(FileRecordRepository fileRecordRepository) {
        this.fileRecordRepository = fileRecordRepository;
    }

    @Override
    public FileRecord findById(Integer id) {
        return fileRecordRepository.findById(id).orElse(null);
    }

    @Override
    public FileRecord save(FileRecord object) {
        return fileRecordRepository.save(object);
    }
}
