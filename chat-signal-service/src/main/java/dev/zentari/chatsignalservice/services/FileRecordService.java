package dev.zentari.chatsignalservice.services;

import dev.zentari.chatsignalservice.models.FileRecord;

public interface FileRecordService {

    FileRecord findById(Integer id);

    FileRecord save(FileRecord object);

}
