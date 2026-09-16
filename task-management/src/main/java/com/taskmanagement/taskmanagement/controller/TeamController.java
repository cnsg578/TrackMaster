package com.taskmanagement.taskmanagement.controller;

import com.taskmanagement.taskmanagement.dto.TeamRequest;
import com.taskmanagement.taskmanagement.dto.TeamResponse;
import com.taskmanagement.taskmanagement.dto.UserResponse;
import com.taskmanagement.taskmanagement.dto.TeamMemberRequest;
import com.taskmanagement.taskmanagement.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(
            @Valid @RequestBody TeamRequest request,
            Authentication authentication) {

        String userEmail = authentication.getName();

        TeamResponse response =
                teamService.createTeam(request, userEmail);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping
public ResponseEntity<List<TeamResponse>> getAllTeams() {

    List<TeamResponse> teams = teamService.getAllTeams();

    return ResponseEntity.ok(teams);
}
@GetMapping("/{teamId}")
public ResponseEntity<TeamResponse> getTeamById(
        @PathVariable Long teamId) {

    TeamResponse response = teamService.getTeamById(teamId);

    return ResponseEntity.ok(response);
}
@PutMapping("/{teamId}")
public ResponseEntity<TeamResponse> updateTeam(
        @PathVariable Long teamId,
        @Valid @RequestBody TeamRequest request) {

    TeamResponse response =
            teamService.updateTeam(teamId, request);

    return ResponseEntity.ok(response);
}
@DeleteMapping("/{teamId}")
public ResponseEntity<Void> deleteTeam(
        @PathVariable Long teamId) {

    teamService.deleteTeam(teamId);

    return ResponseEntity.noContent().build();
}
@PostMapping("/{teamId}/members")
public ResponseEntity<Void> addMember(
        @PathVariable Long teamId,
        @Valid @RequestBody TeamMemberRequest request) {

    teamService.addMember(teamId, request.getUserId());

    return ResponseEntity.status(HttpStatus.CREATED).build();
}
@GetMapping("/{teamId}/members")
public ResponseEntity<List<UserResponse>> getTeamMembers(
        @PathVariable Long teamId) {

    List<UserResponse> members =
            teamService.getTeamMembers(teamId);

    return ResponseEntity.ok(members);
}
@DeleteMapping("/{teamId}/members/{userId}")
public ResponseEntity<Void> removeMember(
        @PathVariable Long teamId,
        @PathVariable Long userId) {

    teamService.removeMember(teamId, userId);

    return ResponseEntity.noContent().build();
}
}