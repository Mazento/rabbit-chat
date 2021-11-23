package dev.zentari.chatnodeservice.repositories;

import dev.zentari.chatnodeservice.models.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRecordRepository extends JpaRepository<FileRecord, Integer> {
}
