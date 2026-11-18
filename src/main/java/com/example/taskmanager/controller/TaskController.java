package com.example.taskmanager.controller;

import com.example.taskmanager.model.TaskEvent;
import com.example.taskmanager.model.dto.CreateTaskRequest;
import com.example.taskmanager.model.dto.UpdateTaskRequest;
import com.example.taskmanager.service.TaskManagementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/managetasks")
public class TaskController {

    @Autowired
    private TaskManagementService taskManagementService;

    @GetMapping("/viewTasks")
    public TaskEvent viewTasks(@NotNull Long requestID) {
        return taskManagementService.viewTaskEvents(requestID);
    }

    @PostMapping("/createTask")
    public String createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        return taskManagementService.createTaskEvent(createTaskRequest);
    }

    @DeleteMapping("/deleteTask")
    public String deleteTask(@NotNull Long requestID) {
        return taskManagementService.deleteTaskEvent(requestID);
    }

    @PutMapping("/updateTask")
    public String updateTask(@NotNull UpdateTaskRequest updateTaskRequest) {
        return taskManagementService.updateTaskEvent(updateTaskRequest);
    }
}


