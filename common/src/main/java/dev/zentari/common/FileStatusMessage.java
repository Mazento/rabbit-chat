package dev.zentari.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class FileStatusMessage implements Serializable {

    public FileStatusMessage(Integer fileId, FileStatus status) {
        this.fileId = fileId;
        this.status = status;
    }

    private Integer fileId;
    private FileStatus status;
    private String url;
}
