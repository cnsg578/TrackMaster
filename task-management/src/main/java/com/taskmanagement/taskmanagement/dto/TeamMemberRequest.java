package com.taskmanagement.taskmanagement.dto;

import jakarta.validation.constraints.NotNull;

public class TeamMemberRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}