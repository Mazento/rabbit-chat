package dev.zentari.chatsignalservice.jms;

import dev.zentari.chatsignalservice.helpers.UserConnectDisconnect;
import dev.zentari.chatsignalservice.helpers.UsersListItem;
import dev.zentari.chatsignalservice.models.ChatMessageDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsService {

    public static final String EXCHANGE_FILE_ATTACHMENT = "file-attachment";
    public static final String EXCHANGE_CHAT_MESSAGE = "chat-message";
    public static final String EXCHANGE_FILE_ATTACHMENT_INTERNAL = "file-attachment-internal";
    public static final String EXCHANGE_USER_CONNECT_DISCONNECT = "user-connect-disconnect";
    public static final String QUEUE_FILE_ATTACHMENT = "file-attachment-fs";

    RabbitTemplate rabbitTemplate;

    public JmsService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendChatMessage(ChatMessageDTO message) {
        rabbitTemplate.convertAndSend(EXCHANGE_CHAT_MESSAGE, "", message);
    }

    public void sendFileStatusUpdate(ChatMessageDTO message) {
        rabbitTemplate.convertAndSend(EXCHANGE_FILE_ATTACHMENT_INTERNAL, "", message);
    }

    public void sendUserListUpdate(String username, UserConnectDisconnect action) {
        UsersListItem usersListItem = new UsersListItem(username, action);
        rabbitTemplate.convertAndSend(EXCHANGE_USER_CONNECT_DISCONNECT, "", usersListItem);
    }
}
