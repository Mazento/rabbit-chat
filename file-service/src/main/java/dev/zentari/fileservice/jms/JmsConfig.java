package dev.zentari.fileservice.jms;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmsConfig {

    public static final String EXCHANGE_FILE_ATTACHMENT = "file-attachment";
    public static final String QUEUE_FILE_ATTACHMENT = "file-attachment-fs";

    @Bean
    Queue queue() {
        return new Queue(QUEUE_FILE_ATTACHMENT, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_FILE_ATTACHMENT);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
