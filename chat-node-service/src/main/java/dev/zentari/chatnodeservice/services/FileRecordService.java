package dev.zentari.chatnodeservice.services;

import dev.zentari.chatnodeservice.models.FileRecord;

public interface FileRecordService {

    FileRecord findById(Integer id);

    FileRecord save(FileRecord object);

}
