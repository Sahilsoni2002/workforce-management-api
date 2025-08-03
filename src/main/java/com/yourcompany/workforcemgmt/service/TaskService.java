package com.yourcompany.workforcemgmt.service;

import com.yourcompany.workforcemgmt.dto.*;
import com.yourcompany.workforcemgmt.mapper.TaskMapper;
import com.yourcompany.workforcemgmt.model.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class containing business logic for task management operations
 * 
 * This service handles all task-related operations including creation, assignment,
 * priority management, commenting, and activity tracking. Uses in-memory storage
 * with HashMap collections as specified in the requirements.
 */
@Service
public class TaskService {
    
    // In-memory storage using HashMap collections
    private final Map<String, Task> tasks = new HashMap<>();
    private final Map<String, Staff> staff = new HashMap<>();
    private final TaskMapper taskMapper;
    
    /**
     * Initialize the service with sample staff data for testing
     */
    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
        initializeSampleData();
    }
    
    /**
     * Initialize sample staff members for testing purposes
     */
    private void initializeSampleData() {
        Staff staff1 = Staff.builder()
                .id("staff1")
                .name("John Doe")
                .email("john.doe@company.com")
                .department("Sales")
                .role("Sales Representative")
                .build();
        
        Staff staff2 = Staff.builder()
                .id("staff2")
                .name("Jane Smith")
                .email("jane.smith@company.com")
                .department("Operations")
                .role("Operations Manager")
                .build();
        
        Staff staff3 = Staff.builder()
                .id("staff3")
                .name("Mike Wilson")
                .email("mike.wilson@company.com")
                .department("Support")
                .role("Customer Support Specialist")
                .build();
        
        staff.put(staff1.getId(), staff1);
        staff.put(staff2.getId(), staff2);
        staff.put(staff3.getId(), staff3);
    }
    
    /**
     * Create a new task
     * 
     * @param request the task creation request
     * @param createdBy the user creating the task
     * @return the created task as DTO
     */
    public TaskDto createTask(CreateTaskRequest request, String createdBy) {
        String taskId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        
        Task task = Task.builder()
                .id(taskId)
                .title(request.getTitle())
                .description(request.getDescription())
                .status(TaskStatus.ACTIVE)
                .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM)
                .assignedStaffId(request.getAssignedStaffId())
                .startDate(request.getStartDate())
                .dueDate(request.getDueDate())
                .createdAt(now)
                .updatedAt(now)
                .createdBy(createdBy)
                .activityHistory(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        
        // Add creation activity log
        addActivityLog(task, createdBy, "CREATED", 
                "Task created and assigned to " + request.getAssignedStaffId());
        
        tasks.put(taskId, task);
        return taskMapper.taskToTaskDto(task);
    }
    
    /**
     * Get all tasks (Bug Fix 2: excludes cancelled tasks)
     * 
     * @return list of active and completed tasks
     */
    public List<TaskDto> getAllTasks() {
        return tasks.values().stream()
                .filter(task -> task.getStatus() != TaskStatus.CANCELLED) // Bug fix: exclude cancelled tasks
                .map(taskMapper::taskToTaskDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a specific task by its ID with complete history
     * 
     * @param id the task ID
     * @return the task with full activity history and comments
     * @throws RuntimeException if task not found
     */
    public TaskDto getTaskById(String id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new RuntimeException("Task not found with ID: " + id);
        }
        
        // Sort activity history and comments chronologically
        task.getActivityHistory().sort(Comparator.comparing(ActivityLog::getTimestamp));
        task.getComments().sort(Comparator.comparing(Comment::getTimestamp));
        
        return taskMapper.taskToTaskDto(task);
    }
    
    /**
     * Get all tasks assigned to a specific staff member (Bug Fix 2: excludes cancelled)
     * 
     * @param staffId the staff member ID
     * @return list of tasks assigned to the staff member
     */
    public List<TaskDto> getTasksByStaffId(String staffId) {
        return tasks.values().stream()
                .filter(task -> staffId.equals(task.getAssignedStaffId()))
                .filter(task -> task.getStatus() != TaskStatus.CANCELLED) // Bug fix: exclude cancelled tasks
                .map(taskMapper::taskToTaskDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Feature 1: Enhanced date-based task fetching (Smart Daily Task View)
     * 
     * Returns tasks that:
     * 1. Started within the specified date range, OR
     * 2. Started before the range but are still active (not completed)
     * 
     * This gives users a true "today's work" view.
     * 
     * @param startDate start of the date range
     * @param endDate end of the date range
     * @return list of relevant tasks for the date range
     */
    public List<TaskDto> getTasksByDateRange(LocalDate startDate, LocalDate endDate) {
        return tasks.values().stream()
                .filter(task -> task.getStatus() != TaskStatus.CANCELLED) // Exclude cancelled tasks
                .filter(task -> {
                    LocalDate taskStartDate = task.getStartDate().toLocalDate();
                    
                    // Return tasks that:
                    // 1. Started within the date range, OR
                    // 2. Started before the range but are still active (not completed)
                    return (taskStartDate.isAfter(startDate.minusDays(1)) && 
                            taskStartDate.isBefore(endDate.plusDays(1))) ||
                           (taskStartDate.isBefore(startDate) && 
                            task.getStatus() == TaskStatus.ACTIVE);
                })
                .map(taskMapper::taskToTaskDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Bug Fix 1: Task reassignment with proper cancellation of old task
     * 
     * This fixes the issue where reassigning tasks created duplicates.
     * Now the original task is cancelled and a new task is created.
     * 
     * @param taskId the task to reassign
     * @param request the reassignment request
     * @return the new task created for the new assignee
     * @throws RuntimeException if task not found
     */
    public TaskDto assignTaskByRef(String taskId, AssignTaskRequest request) {
        Task originalTask = tasks.get(taskId);
        if (originalTask == null) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }
        
        // Cancel the original task
        originalTask.setStatus(TaskStatus.CANCELLED);
        originalTask.setUpdatedAt(LocalDateTime.now());
        addActivityLog(originalTask, request.getReassignedBy(), "CANCELLED", 
                "Task cancelled due to reassignment to " + request.getNewStaffId());
        
        // Create a new task for the new staff member
        String newTaskId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        
        Task newTask = Task.builder()
                .id(newTaskId)
                .title(originalTask.getTitle())
                .description(originalTask.getDescription())
                .status(TaskStatus.ACTIVE)
                .priority(originalTask.getPriority())
                .assignedStaffId(request.getNewStaffId())
                .startDate(originalTask.getStartDate())
                .dueDate(originalTask.getDueDate())
                .createdAt(now)
                .updatedAt(now)
                .createdBy(request.getReassignedBy())
                .activityHistory(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
        
        addActivityLog(newTask, request.getReassignedBy(), "REASSIGNED", 
                "Task reassigned from " + originalTask.getAssignedStaffId() + 
                " to " + request.getNewStaffId());
        
        tasks.put(newTaskId, newTask);
        return taskMapper.taskToTaskDto(newTask);
    }
    
    /**
     * Feature 2: Update task priority
     * 
     * @param taskId the task to update
     * @param request the priority update request
     * @return the updated task
     * @throws RuntimeException if task not found
     */
    public TaskDto updateTaskPriority(String taskId, UpdatePriorityRequest request) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }
        
        TaskPriority oldPriority = task.getPriority();
        task.setPriority(request.getPriority());
        task.setUpdatedAt(LocalDateTime.now());
        
        addActivityLog(task, request.getUpdatedBy(), "PRIORITY_UPDATED", 
                "Priority changed from " + oldPriority + " to " + request.getPriority());
        
        return taskMapper.taskToTaskDto(task);
    }
    
    /**
     * Feature 2: Get tasks by priority level
     * 
     * @param priority the priority level to filter by
     * @return list of tasks with the specified priority
     */
    public List<TaskDto> getTasksByPriority(TaskPriority priority) {
        return tasks.values().stream()
                .filter(task -> task.getPriority() == priority)
                .filter(task -> task.getStatus() != TaskStatus.CANCELLED) // Exclude cancelled tasks
                .map(taskMapper::taskToTaskDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Feature 3: Add comment to task
     * 
     * @param taskId the task to add comment to
     * @param request the comment request
     * @return the updated task with the new comment
     * @throws RuntimeException if task not found
     */
    public TaskDto addCommentToTask(String taskId, AddCommentRequest request) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }
        
        Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .taskId(taskId)
                .userId(request.getUserId())
                .text(request.getText())
                .timestamp(LocalDateTime.now())
                .build();
        
        task.getComments().add(comment);
        task.setUpdatedAt(LocalDateTime.now());
        
        addActivityLog(task, request.getUserId(), "COMMENT_ADDED", 
                "Comment added: \"" + 
                (request.getText().length() > 50 ? 
                 request.getText().substring(0, 50) + "..." : 
                 request.getText()) + "\"");
        
        return taskMapper.taskToTaskDto(task);
    }
    
    /**
     * Update task status with activity logging
     * 
     * @param taskId the task to update
     * @param status the new status
     * @param updatedBy the user updating the status
     * @return the updated task
     * @throws RuntimeException if task not found
     */
    public TaskDto updateTaskStatus(String taskId, TaskStatus status, String updatedBy) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }
        
        TaskStatus oldStatus = task.getStatus();
        task.setStatus(status);
        task.setUpdatedAt(LocalDateTime.now());
        
        addActivityLog(task, updatedBy, "STATUS_UPDATED", 
                "Status changed from " + oldStatus + " to " + status);
        
        return taskMapper.taskToTaskDto(task);
    }
    
    /**
     * Feature 3: Add activity log entry to a task's history
     * 
     * This method automatically tracks all significant events that occur
     * during a task's lifecycle for complete audit trail.
     * 
     * @param task the task to log activity for
     * @param userId the user performing the action
     * @param action the type of action performed
     * @param description detailed description of the action
     */
    private void addActivityLog(Task task, String userId, String action, String description) {
        ActivityLog log = ActivityLog.builder()
                .id(UUID.randomUUID().toString())
                .taskId(task.getId())
                .userId(userId)
                .action(action)
                .description(description)
                .timestamp(LocalDateTime.now())
                .build();
        
        task.getActivityHistory().add(log);
    }
    
    /**
     * Get all staff members (utility method for testing)
     * 
     * @return map of all staff members
     */
    public Map<String, Staff> getAllStaff() {
        return new HashMap<>(staff);
    }
}