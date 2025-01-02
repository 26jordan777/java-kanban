package com.yandex.tracker.test;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.Status;
import com.yandex.tracker.model.TaskType;
import com.yandex.tracker.service.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest  {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testCreateAndGetTask() {
        Task task = new Task(1, TaskType.TASK, "Test Task", Status.NEW, "Test Description",
                Duration.ofMinutes(30), LocalDateTime.now());
        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTask(task.getId());
        assertEquals(task, retrievedTask);
    }

    @Test
    public void testCreateAndGetSubtask() {
        Epic epic = new Epic(2, TaskType.EPIC, "Epic Name", Status.NEW, "Epic Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(3, TaskType.SUBTASK, "Test Subtask", Status.NEW,
                "Test Subtask Description", epic.getId(),
                Duration.ofMinutes(20), LocalDateTime.now());
        taskManager.createSubtask(subtask);

        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        assertEquals(subtask, retrievedSubtask);
    }

    @Test
    public void testCreateAndGetEpic() {
        Epic epic = new Epic(4, TaskType.EPIC, "Epic 1", Status.NEW, "Description of Epic 1");
        taskManager.createEpic(epic);
        Epic retrievedEpic = taskManager.getEpic(epic.getId());
        assertEquals(epic, retrievedEpic);
    }

    @Test
    public void testHistory() {
        Task task1 = taskManager.createTask(new Task(5, TaskType.TASK, "Task 1", Status.NEW, "Description 1",
                Duration.ofMinutes(30), LocalDateTime.now()));
        Task task2 = taskManager.createTask(new Task(6, TaskType.TASK, "Task 2", Status.NEW, "Description 2",
                Duration.ofMinutes(45), LocalDateTime.now().plusHours(1)));

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    public void testHistoryLimit() {
        for (int i = 0; i < 12; i++) {
            taskManager.createTask(new Task(i + 1, TaskType.TASK, "Task " + i, Status.NEW, "Description " + i,
                    Duration.ofMinutes(30), LocalDateTime.now().plusDays(i)));
        }

        for (int i = 0; i < 12; i++) {
            taskManager.getTask(i + 1);
        }

        List<Task> history = taskManager.getHistory();
        assertEquals(10, history.size(), "История должна содержать последние 10 задач");
    }

    @Test
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}