package dev.zentari.fileservice.jms;

import dev.zentari.common.FileStatus;
import dev.zentari.common.FileStatusMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsService {

    private final RabbitTemplate rabbitTemplate;

    public JmsService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Integer fileId, FileStatus status) {
        FileStatusMessage message = new FileStatusMessage(fileId, status);
        sendMessageObject(message);
    }

    public void sendMessage(Integer fileId, FileStatus status, String url) {
        FileStatusMessage message = new FileStatusMessage(fileId, status, url);
        sendMessageObject(message);
    }

    private void sendMessageObject(FileStatusMessage message) {
        rabbitTemplate.convertAndSend(JmsConfig.EXCHANGE_FILE_ATTACHMENT, "", message);
    }
}
