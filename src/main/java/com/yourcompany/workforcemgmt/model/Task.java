package com.yourcompany.workforcemgmt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a task in the workforce management system
 * 
 * A task is a single unit of work that can be assigned to an employee
 * and tracked through its lifecycle from creation to completion.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    
    /**
     * Unique identifier for this task
     */
    private String id;
    
    /**
     * Title/name of the task
     */
    private String title;
    
    /**
     * Detailed description of what needs to be done
     */
    private String description;
    
    /**
     * Current status of the task (ACTIVE, COMPLETED, CANCELLED)
     */
    private TaskStatus status;
    
    /**
     * Priority level of the task (HIGH, MEDIUM, LOW)
     */
    private TaskPriority priority;
    
    /**
     * ID of the staff member assigned to this task
     */
    private String assignedStaffId;
    
    /**
     * When the task should start
     */
    private LocalDateTime startDate;
    
    /**
     * When the task is due to be completed
     */
    private LocalDateTime dueDate;
    
    /**
     * When this task was created
     */
    private LocalDateTime createdAt;
    
    /**
     * When this task was last updated
     */
    private LocalDateTime updatedAt;
    
    /**
     * ID of the user who created this task
     */
    private String createdBy;
    
    /**
     * Complete history of all activities performed on this task
     * Automatically populated when activities occur
     */
    @Builder.Default
    private List<ActivityLog> activityHistory = new ArrayList<>();
    
    /**
     * All user comments added to this task
     * Sorted chronologically when retrieved
     */
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();
}