package dev.zentari.chatsignalservice.controllers;

import dev.zentari.chatsignalservice.jms.JmsService;
import dev.zentari.chatsignalservice.models.ChatMessage;
import dev.zentari.chatsignalservice.models.ChatMessageDTO;
import dev.zentari.chatsignalservice.models.FileRecord;
import dev.zentari.chatsignalservice.models.FileRecordDTO;
import dev.zentari.chatsignalservice.services.ChatMessageService;
import dev.zentari.chatsignalservice.services.FileRecordService;
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

    /**
     * Accepts new message from the client. Persists in the database and sends this message to the message broker.
     * @param message chat message from the client
    */
    @MessageMapping("/message")
    public void newMessage(ChatMessageDTO message) {

        log.debug("New message: " + message.getText());

        ChatMessage chatMessage = ChatMessage.builder()
                .username(message.getUsername())
                .text(message.getText())
                .dateTime(new Timestamp(message.getTimestamp()).toLocalDateTime())
                .build();

        ChatMessage savedChatMessage = chatMessageService.save(chatMessage);

        jmsService.sendChatMessage(ChatMessageDTO.toDTO(savedChatMessage));
    }

    /**
     * Requested during initial client's connection
     * @return list of all saved chat messages
     */
    @MessageMapping("/getChatHistory")
    @SendToUser("/queue/replyChatHistory")
    public List<ChatMessageDTO> getChatHistory() {
        return chatMessageService.findAllRelevant();
    }

    /**
     * Accepts new message with attached file from the client and persist in the database. Attached file only contains
     * the information about the file. File itself is not passed here.
     * @param message chat message from the client
     * @return id of the attached file
     */
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

    /**
     * Get new chat message from the RabbitMQ and send it to all connected clients.
     * <p>This message was send from chat-signal-service.</p>
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "", exclusive = "true", autoDelete = "true"),
            exchange = @Exchange(value = JmsService.EXCHANGE_CHAT_MESSAGE, type = ExchangeTypes.TOPIC))
    )
    public void onNewChatMessageMQ(ChatMessageDTO message) {
        log.debug("newMessageMQ {}", message.getText());
        simpMessagingTemplate.convertAndSend("/queue/message", message);
    }

    /**
     * Get file status update from the RabbitMQ queue. After being processed send message to the RabbitMQ
     * to all chat-signal-services.
     * <p>This message was send from file-service.</p>
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = JmsService.QUEUE_FILE_ATTACHMENT, durable = "false"),
            exchange = @Exchange(value = JmsService.EXCHANGE_FILE_ATTACHMENT, type = ExchangeTypes.TOPIC))
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

    /**
     * Get file status update from the RabbitMQ and send it to all connected clients.
     * <p>This message was send from chat-signal-service.</p>
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "", exclusive = "true", autoDelete = "true"),
            exchange = @Exchange(value = JmsService.EXCHANGE_FILE_ATTACHMENT_INTERNAL, type = ExchangeTypes.TOPIC))
    )
    public void onFileStatusUpdate(ChatMessageDTO message) {
        simpMessagingTemplate.convertAndSend("/queue/fileStatusUpdate", message);
    }
}
