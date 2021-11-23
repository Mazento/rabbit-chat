package dev.zentari.chatnodeservice.repositories;

import dev.zentari.chatnodeservice.models.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface ChatUserRepository extends JpaRepository<ChatUser, String> {

    @Transactional
    void deleteBySessionId(String sessionId);

    ChatUser findBySessionId(String sessionId);

    ChatUser findByUsername(String username);

    boolean existsByUsername(String username);
}
