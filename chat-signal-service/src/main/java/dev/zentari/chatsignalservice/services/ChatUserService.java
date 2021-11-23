package dev.zentari.chatsignalservice.services;

import dev.zentari.chatsignalservice.models.ChatUser;

import java.util.List;

public interface ChatUserService {

    ChatUser save(ChatUser object);

    List<ChatUser> findAll();

    List<String> findAllUsernames();

    ChatUser findBySessionId(String sessionId);

    ChatUser findByUsername(String username);

    boolean existsByUsername(String username);

    void deleteBySessionId(String sessionId);

    void deleteAll();
}
