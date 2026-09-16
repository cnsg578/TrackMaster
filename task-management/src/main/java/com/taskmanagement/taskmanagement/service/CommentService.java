package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.dto.CommentRequest;
import com.taskmanagement.taskmanagement.dto.CommentResponse;
import com.taskmanagement.taskmanagement.entity.Comment;
import com.taskmanagement.taskmanagement.entity.Task;
import com.taskmanagement.taskmanagement.entity.User;
import com.taskmanagement.taskmanagement.repository.CommentRepository;
import com.taskmanagement.taskmanagement.repository.TaskRepository;
import com.taskmanagement.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentService(
            CommentRepository commentRepository,
            TaskRepository taskRepository,
            UserRepository userRepository) {

        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public CommentResponse addComment(
            Long taskId,
            CommentRequest request,
            String userEmail) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Comment comment = new Comment();

        comment.setTask(task);
        comment.setUser(user);
        comment.setContent(request.getContent());

        Comment savedComment = commentRepository.save(comment);

        return toCommentResponse(savedComment);
    }

    public List<CommentResponse> getTaskComments(Long taskId) {

        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Task not found");
        }

        return commentRepository.findByTaskId(taskId)
                .stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse updateComment(
            Long commentId,
            CommentRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new RuntimeException("Comment not found"));

        comment.setContent(request.getContent());

        Comment updatedComment =
                commentRepository.save(comment);

        return toCommentResponse(updatedComment);
    }

    public void deleteComment(Long commentId) {

        if (!commentRepository.existsById(commentId)) {
            throw new RuntimeException("Comment not found");
        }

        commentRepository.deleteById(commentId);
    }

    private CommentResponse toCommentResponse(Comment comment) {

        return new CommentResponse(
                comment.getId(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}