package com.example.taskmanager.repository;

import com.example.taskmanager.model.TaskEvent;
import com.example.taskmanager.model.TaskPriority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
public class TaskEventRepositoryIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:16-alpine")
                    .withDatabaseName("taskmanager_test")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired
    private TaskEventRepository taskEventRepository;

    @Test
    void shouldSaveAndFindTask() {
        TaskEvent task = new TaskEvent();
        task.setTaskName("Integration testing");
        task.setTaskPriority(TaskPriority.HIGH);
        task.setDescription("Test with real PostgreSQL");

        TaskEvent savedTask = taskEventRepository.save(task);

        assertNotNull(savedTask.getId());

        Optional<TaskEvent> result =
                taskEventRepository.findById(savedTask.getId());

        assertTrue(result.isPresent());
        assertEquals("Integration testing",
                result.get().getTaskName());
        assertEquals("HIGH", result.get().getTaskPriority().name());
    }

    @Test
    void shouldDeleteTask() {
        TaskEvent task = new TaskEvent();
        task.setTaskName("Task to delete");
        task.setTaskPriority(TaskPriority.LOW);
        task.setDescription("Delete integration test");

        TaskEvent savedTask = taskEventRepository.save(task);

        taskEventRepository.deleteById(savedTask.getId());

        assertFalse(taskEventRepository.existsById(savedTask.getId()));
    }
}
