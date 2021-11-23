package dev.zentari.chatnodeservice.services;

import dev.zentari.chatnodeservice.models.ChatUser;
import dev.zentari.chatnodeservice.repositories.ChatUserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatUserServiceImpl implements ChatUserService {

    private final ChatUserRepository chatUserRepository;

    public ChatUserServiceImpl(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    public ChatUser save(ChatUser object) {
        return chatUserRepository.save(object);
    }

    @Override
    public List<ChatUser> findAll() {
        return new ArrayList<>(chatUserRepository.findAll());
    }

    @Override
    public List<String> findAllUsernames() {

        List<String> chatUsers = new ArrayList<>();
        chatUserRepository.findAll().forEach(el -> chatUsers.add(el.getUsername()));
        return chatUsers;
    }

    @Override
    public ChatUser findBySessionId(String sessionId) {
        return chatUserRepository.findBySessionId(sessionId);
    }

    @Override
    public ChatUser findByUsername(String username) {
        return chatUserRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return chatUserRepository.existsByUsername(username);
    }

    @Override
    public void deleteBySessionId(String sessionId) {
        chatUserRepository.deleteBySessionId(sessionId);
    }

    @Override
    public void deleteAll() {
        chatUserRepository.deleteAll();
    }
}
