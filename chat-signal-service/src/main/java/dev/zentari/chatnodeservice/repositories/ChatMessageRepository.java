package dev.zentari.chatnodeservice.repositories;

import dev.zentari.chatnodeservice.models.ChatMessage;
import dev.zentari.common.FileStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

    @Query("select cm from ChatMessage cm left join fetch cm.fileRecord fr where fr.status <> :status or fr.status is null")
    List<ChatMessage> findAllRelevant(@Param("status") FileStatus status, Sort dateTime);

    ChatMessage findByFileRecordId(Integer fileRecordId);
}
