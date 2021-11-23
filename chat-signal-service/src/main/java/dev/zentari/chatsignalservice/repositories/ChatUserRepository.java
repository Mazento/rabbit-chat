package dev.zentari.chatsignalservice.repositories;

import dev.zentari.chatsignalservice.models.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface ChatUserRepository extends JpaRepository<ChatUser, String> {

    @Transactional
    void deleteBySessionId(String sessionId);

    ChatUser findBySessionId(String sessionId);

    ChatUser findByUsername(String username);

    boolean existsByUsername(String username);
}
