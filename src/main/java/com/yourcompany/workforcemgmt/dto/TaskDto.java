package com.yourcompany.workforcemgmt.dto;

import com.yourcompany.workforcemgmt.model.TaskPriority;
import com.yourcompany.workforcemgmt.model.TaskStatus;
import com.yourcompany.workforcemgmt.model.ActivityLog;
import com.yourcompany.workforcemgmt.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Task entity
 * 
 * Used for API responses to provide complete task information
 * including activity history and comments
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private String assignedStaffId;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    
    /**
     * Complete activity history for this task, sorted chronologically
     */
    private List<ActivityLog> activityHistory;
    
    /**
     * All user comments for this task, sorted chronologically
     */
    private List<Comment> comments;
}