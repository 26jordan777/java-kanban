package com.yandex.tracker.test;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryTaskManagerTest {
    // Патимат, привет! извиняюсь, почему-то не загрузились данные в классы,
    // сами классы загрузились, но без дынных.
    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testCreateAndGetTask() {
        Task task = new Task("Test Task", "Test Description");
        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTask(task.getId());
        Assertions.assertEquals(task, retrievedTask);
    }

    @Test
    public void testCreateAndGetSubtask() {
        Epic epic = taskManager.createEpic(new Epic("Epic Name", "Epic Description"));
        Subtask subtask = new Subtask(0, "Test Subtask", "Test Subtask Description", epic.getId());
        taskManager.createSubtask(subtask);

        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        Assertions.assertEquals(subtask, retrievedSubtask);
    }

    @Test
    public void testHistory() {
        Task task1 = taskManager.createTask(new Task("Task 1", "Description 1"));
        Task task2 = taskManager.createTask(new Task("Task 2", "Description 2"));

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
            taskManager.createTask(new Task("Task " + i, "Description " + i));
        }

        for (int i = 0; i < 12; i++) {
            taskManager.getTask(i);
        }

        List<Task> history = taskManager.getHistory();
        Assertions.assertEquals(10, history.size());
    }

}