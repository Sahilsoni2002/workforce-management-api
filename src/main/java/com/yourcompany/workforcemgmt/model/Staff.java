package com.yourcompany.workforcemgmt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a staff member in the workforce management system
 * 
 * Staff members are employees who can be assigned to tasks such as
 * salespeople, operations staff, managers, etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    
    /**
     * Unique identifier for this staff member
     */
    private String id;
    
    /**
     * Full name of the staff member
     */
    private String name;
    
    /**
     * Email address for communication
     */
    private String email;
    
    /**
     * Department the staff member belongs to (e.g., Sales, Operations, Support)
     */
    private String department;
    
    /**
     * Job role/title (e.g., Sales Representative, Operations Manager)
     */
    private String role;
}