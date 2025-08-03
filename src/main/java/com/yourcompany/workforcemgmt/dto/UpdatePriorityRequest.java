package com.yourcompany.workforcemgmt.dto;

import com.yourcompany.workforcemgmt.model.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating a task's priority level
 * 
 * Part of Feature 2: Task Priority Management
 * Allows managers to change task priorities after creation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePriorityRequest {
    
    /**
     * New priority level for the task (required)
     */
    private TaskPriority priority;
    
    /**
     * ID of the user updating the priority (required for audit trail)
     */
    private String updatedBy;
}