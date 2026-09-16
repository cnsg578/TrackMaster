package com.taskmanagement.taskmanagement.service;

import com.taskmanagement.taskmanagement.entity.TeamMember;
import com.taskmanagement.taskmanagement.repository.TeamMemberRepository;
import com.taskmanagement.taskmanagement.dto.TeamRequest;
import com.taskmanagement.taskmanagement.dto.TeamResponse;
import com.taskmanagement.taskmanagement.dto.UserResponse;
import com.taskmanagement.taskmanagement.entity.Team;
import com.taskmanagement.taskmanagement.entity.User;
import com.taskmanagement.taskmanagement.repository.TeamRepository;
import com.taskmanagement.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;

   public TeamService(TeamRepository teamRepository,
                   UserRepository userRepository,
                   TeamMemberRepository teamMemberRepository) {
    this.teamRepository = teamRepository;
    this.userRepository = userRepository;
    this.teamMemberRepository = teamMemberRepository;
}

    public TeamResponse createTeam(TeamRequest request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Team team = new Team();

        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team.setCreatedBy(user);

        Team savedTeam = teamRepository.save(team);

        return new TeamResponse(
                savedTeam.getId(),
                savedTeam.getName(),
                savedTeam.getDescription(),
                savedTeam.getCreatedBy().getId(),
                savedTeam.getCreatedAt()
        );
    }
    public List<TeamResponse> getAllTeams() {

    return teamRepository.findAll()
            .stream()
            .map(team -> new TeamResponse(
                    team.getId(),
                    team.getName(),
                    team.getDescription(),
                    team.getCreatedBy().getId(),
                    team.getCreatedAt()
            ))
            .collect(Collectors.toList());
}
    public TeamResponse getTeamById(Long teamId) {

    Team team = teamRepository.findById(teamId)
            .orElseThrow(() ->
                    new RuntimeException("Team not found"));

    return new TeamResponse(
            team.getId(),
            team.getName(),
            team.getDescription(),
            team.getCreatedBy().getId(),
            team.getCreatedAt()
    );
}
public TeamResponse updateTeam(Long teamId, TeamRequest request) {

    Team team = teamRepository.findById(teamId)
            .orElseThrow(() ->
                    new RuntimeException("Team not found"));

    team.setName(request.getName());
    team.setDescription(request.getDescription());

    Team updatedTeam = teamRepository.save(team);

    return new TeamResponse(
            updatedTeam.getId(),
            updatedTeam.getName(),
            updatedTeam.getDescription(),
            updatedTeam.getCreatedBy().getId(),
            updatedTeam.getCreatedAt()
    );
}
public void deleteTeam(Long teamId) {

    if (!teamRepository.existsById(teamId)) {
        throw new RuntimeException("Team not found");
    }

    teamRepository.deleteById(teamId);
}
public void addMember(Long teamId, Long userId) {

    Team team = teamRepository.findById(teamId)
            .orElseThrow(() ->
                    new RuntimeException("Team not found"));

    User user = userRepository.findById(userId)
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    if (teamMemberRepository.existsByTeamIdAndUserId(teamId, userId)) {
        throw new RuntimeException("User is already a member of this team");
    }

    TeamMember teamMember = new TeamMember();

    teamMember.setTeam(team);
    teamMember.setUser(user);
    teamMember.setRole("MEMBER");

    teamMemberRepository.save(teamMember);
}
public List<UserResponse> getTeamMembers(Long teamId) {

    if (!teamRepository.existsById(teamId)) {
        throw new RuntimeException("Team not found");
    }

    return teamMemberRepository.findByTeamId(teamId)
            .stream()
            .map(member -> {
                User user = member.getUser();

                return new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                );
            })
            .collect(Collectors.toList());
}
public void removeMember(Long teamId, Long userId) {

    TeamMember teamMember = teamMemberRepository
            .findByTeamIdAndUserId(teamId, userId)
            .orElseThrow(() ->
                    new RuntimeException("User is not a member of this team"));

    teamMemberRepository.delete(teamMember);
}
}