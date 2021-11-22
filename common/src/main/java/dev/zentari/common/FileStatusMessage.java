package dev.zentari.common;

import java.io.Serializable;

public class FileStatusMessage implements Serializable {

    private Integer fileId;
    private FileStatus status;
    private String url;

    public FileStatusMessage(Integer fileId, FileStatus status) {
        this.fileId = fileId;
        this.status = status;
    }

    public FileStatusMessage(Integer fileId, FileStatus status, String url) {
        this.fileId = fileId;
        this.status = status;
        this.url = url;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public FileStatus getStatus() {
        return status;
    }

    public void setStatus(FileStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
