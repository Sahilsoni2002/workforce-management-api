package com.yourcompany.workforcemgmt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a user comment on a task
 * 
 * Comments allow team members to communicate about task progress,
 * share updates, and provide additional context.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    
    /**
     * Unique identifier for this comment
     */
    private String id;
    
    /**
     * ID of the task this comment belongs to
     */
    private String taskId;
    
    /**
     * ID of the user who wrote this comment
     */
    private String userId;
    
    /**
     * The comment text content
     */
    private String text;
    
    /**
     * When this comment was created
     */
    private LocalDateTime timestamp;
}