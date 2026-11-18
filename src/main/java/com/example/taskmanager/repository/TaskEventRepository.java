package com.example.taskmanager.repository;

import com.example.taskmanager.model.TaskEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskEventRepository extends JpaRepository<TaskEvent, Long> {
}
