package com.example.taskmanager.model.mapper;

import com.example.taskmanager.model.TaskEvent;
import com.example.taskmanager.model.dto.CreateTaskRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
   @Mapping(target = "id", ignore = true)
   TaskEvent toEntity(CreateTaskRequest dto);

}
