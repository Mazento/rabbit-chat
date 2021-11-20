package dev.zentari.chatnodeservice.models;

import dev.zentari.common.FileStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "file_record")
public class FileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String filename;
    private String url;
    private Long size;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "ENUM('INIT', 'PROCESSING', 'SUCCESS', 'FAILURE')")
    private FileStatus status;

    @OneToOne(mappedBy = "fileRecord")
    private ChatMessage chatMessage;
}
