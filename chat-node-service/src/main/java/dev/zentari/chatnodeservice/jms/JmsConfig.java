package dev.zentari.chatnodeservice.jms;

import dev.zentari.chatnodeservice.controllers.MessagesController;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfig {

    public static final String EXCHANGE_FILE_ATTACHMENT = "file-attachment";
    public static final String EXCHANGE_CHAT_MESSAGE = "chat-message";
    public static final String EXCHANGE_FILE_ATTACHMENT_INTERNAL = "file-attachment-internal";
    public static final String EXCHANGE_USER_CONNECT_DISCONNECT = "user-connect-disconnect";
    public static final String QUEUE_FILE_ATTACHMENT = "file-attachment-fs";
    public static final String ROUTE_FILE = "file-route";

    /**
     * Queue for receiving status updates on a file saving process.
     * Messages in this queue are supplied by the file server.
     * Only one chat node will receive an update.
     * This node will save the new status in the database and then will notify other chat nodes.
     * @see #queueFileNodes()
    */
//    @Bean
//    Queue queueFileFS() {
//        return new Queue(QUEUE_FILE_ATTACHMENT, false);
//    }
//
//    /**
//     * Queue for notifying all chat nodes about the status update of the processing file.
//     */
//    @Bean
//    Queue queueFileNodes() {
//        // Auto-generate exclusive queue
//        return new Queue("", false, true, true);
//    }
//
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange(EXCHANGE_FILE_ATTACHMENT);
//    }
//
//    @Bean
//    Binding bindingFileFS(TopicExchange exchange) {
//        return BindingBuilder.bind(queueFileFS()).to(exchange).with("");
//    }
//
//    @Bean
//    Binding bindingFileNodes(TopicExchange exchange) {
//        return BindingBuilder.bind(queueFileNodes()).to(exchange).with(ROUTE_FILE);
//    }
//
//    @Bean
//    MessageListenerAdapter fileListenerAdapter(MessagesController messagesController) {
//        return new MessageListenerAdapter(messagesController, "handleFileServerMessage");
//    }

//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
//
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
////        container.setQueues(queueFileFS(), queueFileNodes());
////        container.setMessageListener(listenerAdapter);
//        return container;
//    }
}
