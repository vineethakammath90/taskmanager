package com.example.taskmanager.service;

import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.TaskEvent;
import com.example.taskmanager.model.TaskPriority;
import com.example.taskmanager.model.dto.UpdateTaskRequest;
import com.example.taskmanager.repository.TaskEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
@DataJpaTest
@ComponentScan(basePackages = "com.example.taskmanager")
public class TaskManagementServiceIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:16-alpine")
                    .withDatabaseName("taskmanager_test")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired
    private TaskManagementService taskManagementService;

    @Autowired
    private TaskEventRepository taskEventRepository;

    @Test
    void shouldRetrieveExistingTask() {
        TaskEvent task = new TaskEvent();
        task.setTaskName("Integration testing");
        task.setTaskPriority(TaskPriority.HIGH);
        task.setDescription("Testing service with PostgreSQL");

        TaskEvent saved = taskEventRepository.save(task);

        TaskEvent result =
                taskManagementService.viewTaskEvents(saved.getId());

        assertNotNull(result);
        assertEquals("Integration testing", result.getTaskName());
    }

    @Test
    void shouldUpdateExistingTask() {
        TaskEvent task = new TaskEvent();
        task.setTaskName("Original task");
        task.setTaskPriority(TaskPriority.LOW);
        task.setDescription("Original description");

        TaskEvent saved = taskEventRepository.save(task);

        UpdateTaskRequest request = mock(UpdateTaskRequest.class);
        when(request.getId()).thenReturn(saved.getId());
        when(request.getTaskName()).thenReturn("Updated task");
        when(request.getTaskPriority()).thenReturn(TaskPriority.HIGH);
        when(request.getDescription()).thenReturn("Updated description");

        taskManagementService.updateTaskEvent(request);

        TaskEvent updated = taskEventRepository.findById(saved.getId())
                .orElseThrow();

        assertEquals("Updated task", updated.getTaskName());
        assertEquals("HIGH", updated.getTaskPriority().name());
        assertEquals("Updated description", updated.getDescription());
    }

    @Test
    void shouldDeleteExistingTask() {
        TaskEvent task = new TaskEvent();
        task.setTaskName("Task to delete");
        task.setTaskPriority(TaskPriority.LOW);
        task.setDescription("Delete test");

        TaskEvent saved = taskEventRepository.save(task);

        taskManagementService.deleteTaskEvent(saved.getId());

        assertFalse(taskEventRepository.existsById(saved.getId()));
    }

    @Test
    void shouldThrowExceptionForMissingTask() {
        assertThrows(
                TaskNotFoundException.class,
                () -> taskManagementService.viewTaskEvents(999999L)
        );
    }
}
