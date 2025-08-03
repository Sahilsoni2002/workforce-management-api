package com.yourcompany.workforcemgmt.mapper;

import com.yourcompany.workforcemgmt.dto.TaskDto;
import com.yourcompany.workforcemgmt.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Manual mapper for converting between Task entities and TaskDto objects
 * 
 * This replaces MapStruct to avoid annotation processor issues during compilation.
 * Provides type-safe mappings between domain objects and DTOs.
 */
@Component
public class TaskMapper {
    
    /**
     * Convert a Task entity to a TaskDto
     * 
     * @param task the Task entity to convert
     * @return the corresponding TaskDto
     */
    public TaskDto taskToTaskDto(Task task) {
        if (task == null) {
            return null;
        }
        
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .assignedStaffId(task.getAssignedStaffId())
                .startDate(task.getStartDate())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .createdBy(task.getCreatedBy())
                .activityHistory(task.getActivityHistory())
                .comments(task.getComments())
                .build();
    }
    
    /**
     * Convert a TaskDto to a Task entity
     * 
     * @param taskDto the TaskDto to convert
     * @return the corresponding Task entity
     */
    public Task taskDtoToTask(TaskDto taskDto) {
        if (taskDto == null) {
            return null;
        }
        
        return Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .priority(taskDto.getPriority())
                .assignedStaffId(taskDto.getAssignedStaffId())
                .startDate(taskDto.getStartDate())
                .dueDate(taskDto.getDueDate())
                .createdAt(taskDto.getCreatedAt())
                .updatedAt(taskDto.getUpdatedAt())
                .createdBy(taskDto.getCreatedBy())
                .activityHistory(taskDto.getActivityHistory())
                .comments(taskDto.getComments())
                .build();
    }
    
    /**
     * Convert a list of Task entities to a list of TaskDto objects
     * 
     * @param tasks the list of Task entities to convert
     * @return the corresponding list of TaskDto objects
     */
    public List<TaskDto> tasksToTaskDtos(List<Task> tasks) {
        if (tasks == null) {
            return null;
        }
        
        return tasks.stream()
                .map(this::taskToTaskDto)
                .collect(Collectors.toList());
    }
}