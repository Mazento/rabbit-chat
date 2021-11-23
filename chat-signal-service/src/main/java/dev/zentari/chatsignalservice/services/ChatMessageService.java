package dev.zentari.chatsignalservice.services;

import dev.zentari.chatsignalservice.models.ChatMessageDTO;
import dev.zentari.chatsignalservice.models.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessage findById(Integer id);

    ChatMessage save(ChatMessage object);

    List<ChatMessageDTO> findAll();

    List<ChatMessageDTO> findAllRelevant();

    ChatMessageDTO findByFileRecordId(Integer fileRecordId);

}
