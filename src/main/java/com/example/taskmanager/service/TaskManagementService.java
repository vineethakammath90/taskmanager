package com.example.taskmanager.service;


import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.TaskEvent;
import com.example.taskmanager.model.dto.CreateTaskRequest;
import com.example.taskmanager.model.dto.UpdateTaskRequest;
import com.example.taskmanager.model.mapper.TaskMapper;
import com.example.taskmanager.repository.TaskEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskManagementService {

    @Autowired
    private TaskEventRepository taskEventRepository;
    @Autowired
    private final TaskMapper taskMapper;

    public TaskManagementService(TaskEventRepository taskEventRepository, TaskMapper taskMapper) {
        this.taskEventRepository = taskEventRepository;
        this.taskMapper = taskMapper;
    }

    public TaskEvent viewTaskEvents(Long requestId) {
        TaskEvent taskEvent = taskEventRepository.findById(requestId).orElseThrow(() ->
                new TaskNotFoundException("Task not found"));
        return taskEvent;
    }

    public String createTaskEvent(CreateTaskRequest request) {
        createTaskFromRequest(request);
        return "Task successfully created";
    }

    public String deleteTaskEvent(Long requestId) {
        taskEventRepository.deleteById(requestId);
        return "Task successfully deleted";
    }

    public String updateTaskEvent(UpdateTaskRequest updateTaskRequest) {
        TaskEvent taskEvent = taskEventRepository.findById(updateTaskRequest.getId()).orElse(null);
        if (null != taskEvent) {
            taskEvent.setTaskName(updateTaskRequest.getTaskName());
            taskEvent.setTaskPriority(updateTaskRequest.getTaskPriority());
            taskEvent.setDescription(updateTaskRequest.getDescription());
            taskEventRepository.save(taskEvent);
        }
        return "Task successfully updated";
    }

    private void createTaskFromRequest(CreateTaskRequest request) {
        TaskEvent taskEvent = taskMapper.toEntity(request);
        taskEventRepository.save(taskEvent);
    }
}
