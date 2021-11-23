package dev.zentari.chatnodeservice.services;

import dev.zentari.chatnodeservice.models.ChatMessageDTO;
import dev.zentari.chatnodeservice.models.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessage findById(Integer id);

    ChatMessage save(ChatMessage object);

    List<ChatMessageDTO> findAll();

    List<ChatMessageDTO> findAllRelevant();

    ChatMessageDTO findByFileRecordId(Integer fileRecordId);

}
