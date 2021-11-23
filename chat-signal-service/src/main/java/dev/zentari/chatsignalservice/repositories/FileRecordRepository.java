package dev.zentari.chatsignalservice.repositories;

import dev.zentari.chatsignalservice.models.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRecordRepository extends JpaRepository<FileRecord, Integer> {
}
