package com.yourcompany.workforcemgmt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for reassigning a task to a different staff member
 * 
 * Used by the assign-by-ref endpoint to handle task reassignments.
 * This implementation fixes the bug where reassignments created duplicates.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignTaskRequest {
    
    /**
     * ID of the staff member to reassign the task to (required)
     */
    private String newStaffId;
    
    /**
     * ID of the user performing the reassignment (required for audit trail)
     */
    private String reassignedBy;
}