package dev.zentari.chatnodeservice.services;

import dev.zentari.chatnodeservice.models.ChatMessageDTO;
import dev.zentari.chatnodeservice.models.ChatMessage;
import dev.zentari.chatnodeservice.repositories.ChatMessageRepository;
import dev.zentari.common.FileStatus;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public ChatMessage findById(Integer id) {
        return chatMessageRepository.findById(id).orElse(null);
    }

    @Override
    public ChatMessage save(ChatMessage object) {
        return chatMessageRepository.save(object);
    }

    @Override
    public List<ChatMessageDTO> findAll() {
        List<ChatMessageDTO> chatMessages = new ArrayList<>();
        chatMessageRepository
                .findAll(Sort.by(Sort.Direction.ASC, "dateTime"))
                .forEach(el -> chatMessages.add(ChatMessageDTO.toDTO(el)));
        return chatMessages;
    }

    @Override
    public List<ChatMessageDTO> findAllRelevant() {
        List<ChatMessageDTO> chatMessages = new ArrayList<>();
        chatMessageRepository
                .findAllRelevant(FileStatus.INIT, Sort.by(Sort.Direction.ASC, "dateTime"))
                .forEach(el -> chatMessages.add(ChatMessageDTO.toDTO(el)));
        return chatMessages;
    }

    @Override
    public ChatMessageDTO findByFileRecordId(Integer fileRecordId) {
        ChatMessage chatMessage = chatMessageRepository.findByFileRecordId(fileRecordId);
        return ChatMessageDTO.toDTO(chatMessage);
    }
}
