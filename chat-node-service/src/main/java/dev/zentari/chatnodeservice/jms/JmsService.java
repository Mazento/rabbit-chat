package dev.zentari.chatnodeservice.jms;

import dev.zentari.chatnodeservice.helpers.UserConnectDisconnect;
import dev.zentari.chatnodeservice.helpers.UsersListItem;
import dev.zentari.chatnodeservice.models.ChatMessageDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsService {

    RabbitTemplate rabbitTemplate;

    public JmsService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendChatMessage(ChatMessageDTO message) {
        rabbitTemplate.convertAndSend(JmsConfig.EXCHANGE_CHAT_MESSAGE, "", message);
    }

    public void sendFileStatusUpdate(ChatMessageDTO message) {
        rabbitTemplate.convertAndSend(JmsConfig.EXCHANGE_FILE_ATTACHMENT_INTERNAL, "", message);
    }

    public void sendUserListUpdate(String username, UserConnectDisconnect action) {
        UsersListItem usersListItem = new UsersListItem(username, action);
        rabbitTemplate.convertAndSend(JmsConfig.EXCHANGE_USER_CONNECT_DISCONNECT, "", usersListItem);
    }
}
