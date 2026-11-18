package com.example.taskmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TaskEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String taskName;
  private String description;
  private TaskPriority taskPriority;
  private LocalDate completionDate;

  public TaskEvent(String taskName, String description, TaskPriority taskPriority, LocalDate completionDate){
      this.taskName = taskName;
      this.description = description;
      this.completionDate = completionDate;
      this.taskPriority = taskPriority;
  }
}
