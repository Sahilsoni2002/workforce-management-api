package com.yourcompany.workforcemgmt.model;

/**
 * Enumeration representing the possible statuses of a task
 */
public enum TaskStatus {
    /**
     * Task is currently active and being worked on
     */
    ACTIVE,
    
    /**
     * Task has been completed successfully
     */
    COMPLETED,
    
    /**
     * Task has been cancelled and will not be completed
     */
    CANCELLED
}