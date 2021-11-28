package dev.zentari.chatsignalservice.controllers;

import dev.zentari.chatsignalservice.helpers.UserConnectDisconnect;
import dev.zentari.chatsignalservice.helpers.UsersListItem;
import dev.zentari.chatsignalservice.jms.JmsService;
import dev.zentari.chatsignalservice.models.ChatUser;
import dev.zentari.chatsignalservice.services.ChatUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    /**
     * Checks if user with provided username already exists
     * @param username name that should be checked for availability
     * @return a {@code Map<String, Boolean>} with the key {@code usernameAvailable}
     */
    @GetMapping("/checkUsername/{username}")
    @ResponseBody
    public Map<String, Boolean> checkUsernameAvailability(@PathVariable String username) {
        boolean userExists = chatUserService.existsByUsername(username);

        log.debug("user {} exists: {}", username, userExists);

        return Collections.singletonMap("usernameAvailable", !userExists);
    }

    /**
     * Saves new user in the database and notifies all clients via RabbitMQ
     * @param username name of the newly connected user
     * @return list of all active users
     */
    @MessageMapping("/getUsersList")
    @SendToUser("/queue/replyUsersList")
    public List<String> getUsersList(String username, SimpMessageHeaderAccessor headerAccessor) {

        log.debug("User joined: " + username);

        ChatUser chatUser = new ChatUser(headerAccessor.getSessionId(), username);
        chatUserService.save(chatUser);

        jmsService.sendUserListUpdate(chatUser.getUsername(), UserConnectDisconnect.CONNECT);

        return chatUserService.findAllUsernames();
    }

    /**
     * Listener for clients disconnect event. Remove user from the database and send message to the RabbitMQ
     */
    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {

        log.debug("Client with session {} disconnected", event.getSessionId());

        ChatUser chatUser = chatUserService.findBySessionId(event.getSessionId());

        jmsService.sendUserListUpdate(chatUser.getUsername(), UserConnectDisconnect.DISCONNECT);

        chatUserService.deleteBySessionId(event.getSessionId());
    }

    /**
     * Get user connect/disconnect event from the RabbitMQ and notify all connected clients.
     * <p>This message was send from chat-signal-service.</p>
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "", exclusive = "true", autoDelete = "true"),
            exchange = @Exchange(value = JmsService.EXCHANGE_USER_CONNECT_DISCONNECT, type = ExchangeTypes.TOPIC))
    )
    public void onUserConnectDisconnectMQ(UsersListItem message) {

        log.debug("onUserConnectDisconnectMQ user: {}, action: {}", message.getUsername(), message.getAction());

        if (message.getAction() == UserConnectDisconnect.CONNECT) {
            simpMessagingTemplate.convertAndSend("/queue/usersListAdd", message.getUsername());
        } else {
            simpMessagingTemplate.convertAndSend("/queue/usersListRemove", message.getUsername());
        }
    }
}
