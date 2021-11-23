package dev.zentari.chatsignalservice.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String text;

    @Column(name = "date")
    private LocalDateTime dateTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_record_id", referencedColumnName = "id")
    private FileRecord fileRecord;
}
