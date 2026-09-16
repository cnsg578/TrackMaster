package com.taskmanagement.taskmanagement.dto;

import java.time.LocalDateTime;

public class AttachmentResponse {

    private Long id;
    private Long taskId;
    private Long uploadedBy;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private LocalDateTime createdAt;

    public AttachmentResponse(
            Long id,
            Long taskId,
            Long uploadedBy,
            String fileName,
            String fileUrl,
            String fileType,
            LocalDateTime createdAt) {

        this.id = id;
        this.taskId = taskId;
        this.uploadedBy = uploadedBy;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public Long getUploadedBy() {
        return uploadedBy;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getFileType() {
        return fileType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}