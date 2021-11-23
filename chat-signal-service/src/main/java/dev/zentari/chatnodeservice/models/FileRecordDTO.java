package dev.zentari.chatnodeservice.models;

import dev.zentari.common.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FileRecordDTO implements Serializable {

    private Integer id;
    private String filename;
    private String url;
    private Long size;
    private FileStatus status;

    public static FileRecordDTO toDTO(FileRecord fileRecord) {
        return FileRecordDTO.builder()
                .id(fileRecord.getId())
                .filename(fileRecord.getFilename())
                .url(fileRecord.getUrl())
                .size(fileRecord.getSize())
                .status(fileRecord.getStatus())
                .build();
    }
}
