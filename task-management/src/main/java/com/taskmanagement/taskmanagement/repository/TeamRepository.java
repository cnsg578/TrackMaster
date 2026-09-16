package com.taskmanagement.taskmanagement.repository;

import com.taskmanagement.taskmanagement.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}