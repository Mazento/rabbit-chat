package dev.zentari.chatsignalservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatMessageDTO implements Serializable {

    private Integer id;
    private String username;
    private String text;
    private long timestamp;
    private boolean hasFileAttachment;
    private FileRecordDTO fileRecord;

    public static ChatMessageDTO toDTO(ChatMessage chatMessage) {

        FileRecordDTO fileRecordDTO = null;

        if (chatMessage.getFileRecord() != null) {
            fileRecordDTO = FileRecordDTO.toDTO(chatMessage.getFileRecord());
        }

        return ChatMessageDTO.builder()
                .id(chatMessage.getId())
                .username(chatMessage.getUsername())
                .text(chatMessage.getText())
                .timestamp(Timestamp.valueOf(chatMessage.getDateTime()).getTime())
                .hasFileAttachment(fileRecordDTO != null)
                .fileRecord(fileRecordDTO)
                .build();
    }
}
