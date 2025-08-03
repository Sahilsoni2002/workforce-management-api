package com.yourcompany.workforcemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for adding a comment to a task
 * 
 * Part of Feature 3: Task Comments & Activity History
 * Allows team members to add free-text comments for communication
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequest {
    
    /**
     * The comment text content (required)
     */
    private String text;
    
    /**
     * ID of the user adding the comment (required for audit trail)
     */
    private String userId;
}