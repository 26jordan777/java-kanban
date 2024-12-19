package com.yandex.tracker.test;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.Status;
import com.yandex.tracker.model.TaskType;
import com.yandex.tracker.service.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testCreateAndGetTask() {
        Task task = new Task(1, TaskType.TASK, "Test Task", Status.NEW, "Test Description");
        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTask(task.getId());
        Assertions.assertEquals(task, retrievedTask);
    }

    @Test
    public void testCreateAndGetSubtask() {
        Epic epic = new Epic(2, TaskType.EPIC, "Epic Name", Status.NEW, "Epic Description");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(3, TaskType.SUBTASK, "Test Subtask", Status.NEW, "Test Subtask Description", epic.getId());
        taskManager.createSubtask(subtask);

        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        Assertions.assertEquals(subtask, retrievedSubtask);
    }

    @Test
    public void testHistory() {
        Task task1 = taskManager.createTask(new Task(4, TaskType.TASK, "Task 1", Status.NEW, "Description 1"));
        Task task2 = taskManager.createTask(new Task(5, TaskType.TASK, "Task 2", Status.NEW, "Description 2"));

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());

        List<Task> history = taskManager.getHistory();
        Assertions.assertEquals(2, history.size());
        Assertions.assertEquals(task1, history.get(0));
        Assertions.assertEquals(task2, history.get(1));
    }

    @Test
    public void testHistoryLimit() {
        for (int i = 0; i < 12; i++) {
            taskManager.createTask(new Task(i + 6, TaskType.TASK, "Task " + i, Status.NEW, "Description " + i));
        }

        for (int i = 0; i < 12; i++) {
            taskManager.getTask(i + 6);
        }

        List<Task> history = taskManager.getHistory();
        Assertions.assertEquals(10, history.size(), "History should contain only the last 10 tasks");
    }
}