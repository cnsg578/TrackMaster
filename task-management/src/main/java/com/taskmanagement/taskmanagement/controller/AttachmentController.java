package com.taskmanagement.taskmanagement.controller;

import com.taskmanagement.taskmanagement.dto.AttachmentResponse;
import com.taskmanagement.taskmanagement.service.AttachmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping
    public ResponseEntity<AttachmentResponse> uploadAttachment(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        String userEmail = authentication.getName();

        AttachmentResponse response =
                attachmentService.uploadAttachment(
                        taskId,
                        file,
                        userEmail
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<AttachmentResponse>> getTaskAttachments(
            @PathVariable Long taskId) {

        List<AttachmentResponse> attachments =
                attachmentService.getTaskAttachments(taskId);

        return ResponseEntity.ok(attachments);
    }

    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable Long taskId,
            @PathVariable Long attachmentId) {

        attachmentService.deleteAttachment(attachmentId);

        return ResponseEntity.noContent().build();
    }
}