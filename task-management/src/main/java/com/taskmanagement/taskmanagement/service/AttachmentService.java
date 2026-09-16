package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.dto.AttachmentResponse;
import com.taskmanagement.taskmanagement.entity.Attachment;
import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.User;
import com.taskmanagement.taskmanagement.repository.AttachmentRepository;
import com.taskmanagement.taskmanagement.repository.TaskRepository;
import com.taskmanagement.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final Path uploadDirectory =
            Paths.get("uploads");

    public AttachmentService(
            AttachmentRepository attachmentRepository,
            TaskRepository taskRepository,
            UserRepository userRepository) {

        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public AttachmentResponse uploadAttachment(
            Long taskId,
            MultipartFile file,
            String userEmail) throws IOException {

        if (file.isEmpty()) {
            throw new RuntimeException("File cannot be empty");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Files.createDirectories(uploadDirectory);

        String originalFileName = file.getOriginalFilename();

        String storedFileName =
                UUID.randomUUID() + "_" + originalFileName;

        Path filePath =
                uploadDirectory.resolve(storedFileName);

        Files.copy(file.getInputStream(), filePath);

        Attachment attachment = new Attachment();

        attachment.setTask(task);
        attachment.setUploadedBy(user);
        attachment.setFileName(originalFileName);
        attachment.setFileUrl("/uploads/" + storedFileName);
        attachment.setFileType(file.getContentType());

        Attachment savedAttachment =
                attachmentRepository.save(attachment);

        return toAttachmentResponse(savedAttachment);
    }

    public List<AttachmentResponse> getTaskAttachments(Long taskId) {

        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found");
        }

        return attachmentRepository.findByTaskId(taskId)
                .stream()
                .map(this::toAttachmentResponse)
                .collect(Collectors.toList());
    }

    public void deleteAttachment(Long attachmentId) {

        Attachment attachment =
                attachmentRepository.findById(attachmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Attachment not found"));

        try {
            String fileUrl = attachment.getFileUrl();

            if (fileUrl != null && fileUrl.startsWith("/uploads/")) {

                String fileName =
                        fileUrl.substring("/uploads/".length());

                Path filePath =
                        uploadDirectory.resolve(fileName);

                Files.deleteIfExists(filePath);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to delete file", e);
        }

        attachmentRepository.delete(attachment);
    }

    private AttachmentResponse toAttachmentResponse(
            Attachment attachment) {

        return new AttachmentResponse(
                attachment.getId(),
                attachment.getTask().getId(),
                attachment.getUploadedBy().getId(),
                attachment.getFileName(),
                attachment.getFileUrl(),
                attachment.getFileType(),
                attachment.getCreatedAt()
        );
    }
}