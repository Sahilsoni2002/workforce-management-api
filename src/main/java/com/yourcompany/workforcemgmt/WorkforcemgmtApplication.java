package com.yourcompany.workforcemgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application class for Workforce Management API
 * 
 * This application provides REST APIs for managing tasks, staff assignments,
 * and workforce operations in a logistics super-app environment.
 */
@SpringBootApplication
public class WorkforcemgmtApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(WorkforcemgmtApplication.class, args);
        System.out.println("🚀 Workforce Management API is running on http://localhost:8080");
        System.out.println("📋 Available endpoints:");
        System.out.println("   GET    /api/tasks - Get all tasks");
        System.out.println("   POST   /api/tasks - Create new task");
        System.out.println("   GET    /api/tasks/{id} - Get task details");
        System.out.println("   POST   /api/tasks/{id}/assign-by-ref - Reassign task");
        System.out.println("   PUT    /api/tasks/{id}/priority - Update priority");
        System.out.println("   POST   /api/tasks/{id}/comments - Add comment");
        System.out.println("   GET    /api/tasks/priority/{priority} - Filter by priority");
        System.out.println("   GET    /api/tasks/date-range - Smart daily view");
    }
}