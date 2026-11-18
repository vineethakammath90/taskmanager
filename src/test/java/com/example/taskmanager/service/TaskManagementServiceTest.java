package com.example.taskmanager.service;

import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.TaskEvent;
import com.example.taskmanager.model.TaskPriority;
import com.example.taskmanager.model.dto.CreateTaskRequest;
import com.example.taskmanager.model.dto.UpdateTaskRequest;
import com.example.taskmanager.model.mapper.TaskMapper;
import com.example.taskmanager.repository.TaskEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskManagementServiceTest {

    @Mock
    private TaskEventRepository taskEventRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskManagementService taskManagementService;

    private TaskEvent taskEvent;

    @BeforeEach
    void setUp() {
        taskEvent = new TaskEvent();
        taskEvent.setTaskName("Learn unit testing");
        taskEvent.setTaskPriority(TaskPriority.HIGH);
        taskEvent.setDescription("Practice JUnit and Mockito");
    }

    @Test
    void shouldReturnTaskWhenTaskExists() {
        when(taskEventRepository.findById(1L))
                .thenReturn(Optional.of(taskEvent));

        TaskEvent result = taskManagementService.viewTaskEvents(1L);

        assertNotNull(result);
        assertEquals("Learn unit testing", result.getTaskName());
        verify(taskEventRepository).findById(1L);
    }

    @Test
    void shouldReturnNullWhenTaskDoesNotExist() {
        when(taskEventRepository.findById(99L))
                .thenThrow(new TaskNotFoundException("Task not found"));

        assertThrows(TaskNotFoundException.class,()->taskManagementService.viewTaskEvents(99L) );
        verify(taskEventRepository).findById(99L);
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        CreateTaskRequest request = mock(CreateTaskRequest.class);

        when(taskMapper.toEntity(request)).thenReturn(taskEvent);

        String result = taskManagementService.createTaskEvent(request);

        assertEquals("Task successfully created", result);
        verify(taskMapper).toEntity(request);
        verify(taskEventRepository).save(taskEvent);
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        String result = taskManagementService.deleteTaskEvent(1L);

        assertEquals("Task successfully deleted", result);
        verify(taskEventRepository).deleteById(1L);
    }

    @Test
    void shouldUpdateTaskWhenTaskExists() {
        UpdateTaskRequest request = mock(UpdateTaskRequest.class);

        when(request.getId()).thenReturn(1L);
        when(request.getTaskName()).thenReturn("Updated task");
        when(request.getTaskPriority()).thenReturn(TaskPriority.LOW);
        when(request.getDescription()).thenReturn("Updated description");

        when(taskEventRepository.findById(1L))
                .thenReturn(Optional.of(taskEvent));

        String result = taskManagementService.updateTaskEvent(request);

        assertEquals("Task successfully updated", result);
        assertEquals("Updated task", taskEvent.getTaskName());
        assertEquals("LOW", taskEvent.getTaskPriority().name());
        assertEquals("Updated description", taskEvent.getDescription());

        verify(taskEventRepository).save(taskEvent);
    }

    @Test
    void shouldNotSaveWhenUpdatingNonexistentTask() {
        UpdateTaskRequest request = mock(UpdateTaskRequest.class);
        when(request.getId()).thenReturn(99L);

        when(taskEventRepository.findById(99L))
                .thenReturn(Optional.empty());

        String result = taskManagementService.updateTaskEvent(request);

        assertEquals("Task successfully updated", result);
        verify(taskEventRepository, never()).save(any(TaskEvent.class));
    }
}
