package com.yourcompany.workforcemgmt.dto;

import com.yourcompany.workforcemgmt.model.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Request DTO for creating a new task
 * 
 * Contains all the necessary information to create a task.
 * Status will default to ACTIVE and priority to MEDIUM if not specified.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    
    /**
     * Title/name of the task (required)
     */
    private String title;
    
    /**
     * Detailed description of the task (optional)
     */
    private String description;
    
    /**
     * ID of the staff member to assign this task to (required)
     */
    private String assignedStaffId;
    
    /**
     * When the task should start (required)
     */
    private LocalDateTime startDate;
    
    /**
     * When the task is due (required)
     */
    private LocalDateTime dueDate;
    
    /**
     * Priority level (optional, defaults to MEDIUM)
     */
    private TaskPriority priority;
}