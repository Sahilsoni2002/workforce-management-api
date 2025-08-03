package com.yourcompany.workforcemgmt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing an activity log entry for tracking task changes
 * 
 * This class captures all significant events that occur during a task's lifecycle
 * such as creation, status changes, reassignments, priority updates, etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityLog {
    
    /**
     * Unique identifier for this activity log entry
     */
    private String id;
    
    /**
     * ID of the task this activity relates to
     */
    private String taskId;
    
    /**
     * ID of the user who performed this action
     */
    private String userId;
    
    /**
     * Type of action performed (e.g., CREATED, STATUS_UPDATED, REASSIGNED)
     */
    private String action;
    
    /**
     * Detailed description of what happened
     */
    private String description;
    
    /**
     * When this activity occurred
     */
    private LocalDateTime timestamp;
}