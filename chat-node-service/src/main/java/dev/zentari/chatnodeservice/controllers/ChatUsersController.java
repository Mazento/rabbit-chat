package dev.zentari.chatnodeservice.controllers;

import dev.zentari.chatnodeservice.helpers.UserConnectDisconnect;
import dev.zentari.chatnodeservice.helpers.UsersListItem;
import dev.zentari.chatnodeservice.jms.JmsConfig;
import dev.zentari.chatnodeservice.jms.JmsService;
import dev.zentari.chatnodeservice.models.ChatUser;
import dev.zentari.chatnodeservice.services.ChatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

@Slf4j
@Controller
public class ChatUsersController {

    private final ChatUserService chatUserService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JmsService jmsService;

    public ChatUsersController(ChatUserService chatUserService,
                               SimpMessagingTemplate simpMessagingTemplate,
                               JmsService jmsService) {

        this.chatUserService = chatUserService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.jmsService = jmsService;
    }

    @GetMapping("/checkUsername/{username}")
    public ResponseEntity<Boolean> checkUsernameAvailability(@PathVariable String username) {
        boolean userExists = chatUserService.existsByUsername(username);
        log.debug("user {} exists: {}", username, userExists);
        return ResponseEntity.ok().body(!userExists);
    }

    @MessageMapping("/getUsersList")
    @SendToUser("/queue/replyUsersList")
    public List<String> getUsersList(String username, SimpMessageHeaderAccessor headerAccessor) {

        log.debug("User joined: " + username);

        ChatUser chatUser = new ChatUser(headerAccessor.getSessionId(), username);
        chatUserService.save(chatUser);

        jmsService.sendUserListUpdate(chatUser.getUsername(), UserConnectDisconnect.CONNECT);

        return chatUserService.findAllUsernames();
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        log.debug("Client with session {} disconnected", event.getSessionId());

        ChatUser chatUser = chatUserService.findBySessionId(event.getSessionId());

        jmsService.sendUserListUpdate(chatUser.getUsername(), UserConnectDisconnect.DISCONNECT);

        chatUserService.deleteBySessionId(event.getSessionId());
    }

    @RabbitListener(bindings =
        @QueueBinding(
                value = @Queue(value = "", exclusive = "true", autoDelete = "true"),
                exchange = @Exchange(value = JmsConfig.EXCHANGE_USER_CONNECT_DISCONNECT, type = ExchangeTypes.TOPIC)
        )
    )
    public void onUserConnectDisconnectMQ(UsersListItem message) {

        log.debug("onUserConnectDisconnectMQ user: {}, action: {}", message.getUsername(), message.getAction());

        if (message.getAction() == UserConnectDisconnect.CONNECT) {
            simpMessagingTemplate.convertAndSend("/queue/usersListAdd", message.getUsername());
        } else {
            simpMessagingTemplate.convertAndSend("/queue/usersListRemove",  message.getUsername());
        }
    }
}
