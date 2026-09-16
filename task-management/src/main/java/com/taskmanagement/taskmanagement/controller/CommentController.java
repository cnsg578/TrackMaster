package com.taskmanagement.taskmanagement.controller;

import com.taskmanagement.taskmanagement.dto.CommentRequest;
import com.taskmanagement.taskmanagement.dto.CommentResponse;
import com.taskmanagement.taskmanagement.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {

        String userEmail = authentication.getName();

        CommentResponse response =
                commentService.addComment(
                        taskId,
                        request,
                        userEmail
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getTaskComments(
            @PathVariable Long taskId) {

        List<CommentResponse> comments =
                commentService.getTaskComments(taskId);

        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request) {

        CommentResponse response =
                commentService.updateComment(
                        commentId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long taskId,
            @PathVariable Long commentId) {

        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }
}
