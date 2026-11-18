package com.example.taskmanager.model.dto;

import com.example.taskmanager.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateTaskRequest {
    @Size(max =100)
    @NotBlank(message ="Task name is required")
    private String taskName;
    private LocalDate completionDate;
    private TaskPriority taskPriority;
    @Size(max =250)
    private String description;
}
