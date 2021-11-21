package dev.zentari.chatnodeservice.controllers;

import dev.zentari.chatnodeservice.jms.JmsConfig;
import dev.zentari.chatnodeservice.jms.JmsService;
import dev.zentari.chatnodeservice.models.ChatMessage;
import dev.zentari.chatnodeservice.models.ChatMessageDTO;
import dev.zentari.chatnodeservice.models.FileRecord;
import dev.zentari.chatnodeservice.models.FileRecordDTO;
import dev.zentari.chatnodeservice.services.ChatMessageService;
import dev.zentari.chatnodeservice.services.FileRecordService;
import dev.zentari.common.FileStatus;
import dev.zentari.common.FileStatusMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Controller
public class MessagesController {

    private final ChatMessageService chatMessageService;
    private final FileRecordService fileRecordService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JmsService jmsService;

    public MessagesController(
            ChatMessageService chatMessageService,
            FileRecordService fileRecordService,
            SimpMessagingTemplate simpMessagingTemplate,
            JmsService jmsService) {

        this.chatMessageService = chatMessageService;
        this.fileRecordService = fileRecordService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.jmsService = jmsService;
    }

    @MessageMapping("/message")
    public ChatMessageDTO newMessage(ChatMessageDTO message) {

        log.debug("New message: " + message.getText());

        ChatMessage chatMessage = ChatMessage.builder()
                .username(message.getUsername())
                .text(message.getText())
                .dateTime(new Timestamp(message.getTimestamp()).toLocalDateTime())
                .build();

        ChatMessage savedChatMessage = chatMessageService.save(chatMessage);

        jmsService.sendChatMessage(ChatMessageDTO.toDTO(savedChatMessage));

        return ChatMessageDTO.toDTO(savedChatMessage);
    }

    @MessageMapping("/getChatHistory")
    @SendToUser("/queue/replyChatHistory")
    public List<ChatMessageDTO> getChatHistory() {
        return chatMessageService.findAllRelevant();
    }

    @MessageMapping("/fileMessage")
    @SendToUser("/queue/replyFileMessage")
    public Integer newFileMessage(ChatMessageDTO message) {

        log.debug("New file message: " + message);
        if (message.getFileRecord() != null) {
            log.debug("Attached file: " + message.getFileRecord().getFilename());
        }

        FileRecordDTO fileRecordDTO = message.getFileRecord();
        FileRecord fileRecord = FileRecord.builder()
                .filename(fileRecordDTO.getFilename())
                .size(fileRecordDTO.getSize())
                .status(FileStatus.INIT)
                .build();

        ChatMessage chatMessage = ChatMessage.builder()
                .username(message.getUsername())
                .text(message.getText())
                .dateTime(new Timestamp(message.getTimestamp()).toLocalDateTime())
                .fileRecord(fileRecord)
                .build();
        ChatMessage savedChatMessage = chatMessageService.save(chatMessage);

        return savedChatMessage.getFileRecord().getId();
    }

    @RabbitListener(bindings =
        @QueueBinding(
                value = @Queue(value = "", exclusive = "true", autoDelete = "true"),
                exchange = @Exchange(value = JmsConfig.EXCHANGE_CHAT_MESSAGE, type = ExchangeTypes.TOPIC)
        )
    )
    public void onNewChatMessageMQ(ChatMessageDTO message) {
        log.debug("newMessageMQ {}", message.getText());
        simpMessagingTemplate.convertAndSend("/queue/message", message);
    }

    @RabbitListener(bindings =
        @QueueBinding(
                value = @Queue(value = "", exclusive = "true", autoDelete = "true"),
                exchange = @Exchange(value = JmsConfig.EXCHANGE_FILE_ATTACHMENT_INTERNAL, type = ExchangeTypes.TOPIC)
        )
    )
    public void onFileStatusUpdate(ChatMessageDTO message) {
        simpMessagingTemplate.convertAndSend("/queue/fileStatusUpdate", message);
    }

    @RabbitListener(bindings =
        @QueueBinding(
                value = @Queue(value = JmsConfig.QUEUE_FILE_ATTACHMENT, durable = "false"),
                exchange = @Exchange(value = JmsConfig.EXCHANGE_FILE_ATTACHMENT, type = ExchangeTypes.TOPIC)
        )
    )
    public void onFileServerMessage(FileStatusMessage message) {

        log.debug("Received file id: {}, status: {}", message.getFileId(), message.getStatus());

        FileRecord fileRecord = fileRecordService.findById(message.getFileId());
        fileRecord.setStatus(message.getStatus());
        fileRecord.setUrl(message.getUrl());
        fileRecordService.save(fileRecord);

        ChatMessageDTO chatMessageDTO = chatMessageService.findByFileRecordId(fileRecord.getId());

        jmsService.sendFileStatusUpdate(chatMessageDTO);
    }
}
