package com.yourcompany.workforcemgmt.controller;

import com.yourcompany.workforcemgmt.dto.*;
import com.yourcompany.workforcemgmt.model.TaskPriority;
import com.yourcompany.workforcemgmt.model.TaskStatus;
import com.yourcompany.workforcemgmt.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @PostMapping
    public ResponseEntity<TaskDto> createTask(@RequestBody CreateTaskRequest request,
                                            @RequestParam(defaultValue = "system") String createdBy) {
        System.out.println("🔵 API CALL: POST /api/tasks - Creating task: " + request.getTitle());
        try {
            TaskDto task = taskService.createTask(request, createdBy);
            System.out.println("✅ SUCCESS: Task created with ID: " + task.getId());
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            System.out.println("❌ ERROR: Failed to create task - " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        System.out.println("🔵 API CALL: GET /api/tasks - Getting all tasks");
        List<TaskDto> tasks = taskService.getAllTasks();
        System.out.println("✅ SUCCESS: Returning " + tasks.size() + " tasks");
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable String id) {
        System.out.println("🔵 API CALL: GET /api/tasks/" + id + " - Getting task details");
        try {
            TaskDto task = taskService.getTaskById(id);
            System.out.println("✅ SUCCESS: Found task: " + task.getTitle());
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            System.out.println("❌ ERROR: Task not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<TaskDto>> getTasksByStaffId(@PathVariable String staffId) {
        System.out.println("🔵 API CALL: GET /api/tasks/staff/" + staffId + " - Getting tasks for staff");
        List<TaskDto> tasks = taskService.getTasksByStaffId(staffId);
        System.out.println("✅ SUCCESS: Found " + tasks.size() + " tasks for staff " + staffId);
        return ResponseEntity.ok(tasks);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<TaskDto>> getTasksByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        System.out.println("🔵 API CALL: GET /api/tasks/date-range - Smart daily view from " + startDate + " to " + endDate);
        List<TaskDto> tasks = taskService.getTasksByDateRange(startDate, endDate);
        System.out.println("✅ SUCCESS: Smart view returning " + tasks.size() + " tasks");
        return ResponseEntity.ok(tasks);
    }
    
    @PostMapping("/{id}/assign-by-ref")
    public ResponseEntity<TaskDto> assignTaskByRef(@PathVariable String id,
                                                 @RequestBody AssignTaskRequest request) {
        System.out.println("🔵 API CALL: POST /api/tasks/" + id + "/assign-by-ref - Reassigning to " + request.getNewStaffId());
        try {
            TaskDto task = taskService.assignTaskByRef(id, request);
            System.out.println("✅ SUCCESS: Task reassigned. New task ID: " + task.getId());
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            System.out.println("❌ ERROR: Failed to reassign task - " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/priority")
    public ResponseEntity<TaskDto> updateTaskPriority(@PathVariable String id,
                                                    @RequestBody UpdatePriorityRequest request) {
        System.out.println("🔵 API CALL: PUT /api/tasks/" + id + "/priority - Updating to " + request.getPriority());
        try {
            TaskDto task = taskService.updateTaskPriority(id, request);
            System.out.println("✅ SUCCESS: Priority updated to " + task.getPriority());
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            System.out.println("❌ ERROR: Failed to update priority - " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskDto>> getTasksByPriority(@PathVariable TaskPriority priority) {
        System.out.println("🔵 API CALL: GET /api/tasks/priority/" + priority + " - Filtering by priority");
        List<TaskDto> tasks = taskService.getTasksByPriority(priority);
        System.out.println("✅ SUCCESS: Found " + tasks.size() + " tasks with " + priority + " priority");
        return ResponseEntity.ok(tasks);
    }
    
    @PostMapping("/{id}/comments")
    public ResponseEntity<TaskDto> addCommentToTask(@PathVariable String id,
                                                  @RequestBody AddCommentRequest request) {
        System.out.println("🔵 API CALL: POST /api/tasks/" + id + "/comments - Adding comment by " + request.getUserId());
        try {
            TaskDto task = taskService.addCommentToTask(id, request);
            System.out.println("✅ SUCCESS: Comment added. Total comments: " + task.getComments().size());
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            System.out.println("❌ ERROR: Failed to add comment - " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskDto> updateTaskStatus(@PathVariable String id,
                                                  @RequestParam TaskStatus status,
                                                  @RequestParam(defaultValue = "system") String updatedBy) {
        System.out.println("🔵 API CALL: PUT /api/tasks/" + id + "/status - Updating to " + status);
        try {
            TaskDto task = taskService.updateTaskStatus(id, status, updatedBy);
            System.out.println("✅ SUCCESS: Status updated to " + task.getStatus());
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            System.out.println("❌ ERROR: Failed to update status - " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/staff")
    public ResponseEntity<?> getAllStaff() {
        System.out.println("🔵 API CALL: GET /api/tasks/staff - Getting all staff members");
        var staff = taskService.getAllStaff();
        System.out.println("✅ SUCCESS: Returning " + staff.size() + " staff members");
        return ResponseEntity.ok(staff);
    }
}