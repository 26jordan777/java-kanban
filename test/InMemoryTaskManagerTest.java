package com.yandex.tracker.test;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Status;
import com.yandex.tracker.model.TaskType;
import com.yandex.tracker.service.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testCreateAndGetTask() {
        Task task = new Task(1, TaskType.TASK, "Тестовая задача", Status.NEW, "Описание теста");
        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTask(task.getId());
        assertEquals(task, retrievedTask, "Должны получить сохраненную задачу");
    }

    @Test
    public void testCreateAndGetSubtask() {
        Epic epic = new Epic(2, TaskType.EPIC, "Эпик", Status.NEW, "Описание эпика");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(3, TaskType.SUBTASK, "Тестовая подзадача", Status.NEW, "Описание подзадачи", epic.getId());
        taskManager.createSubtask(subtask);

        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        assertEquals(subtask, retrievedSubtask, "Должны получить сохраненную подзадачу");
    }

    @Test
    public void testHistory() {
        Task task1 = taskManager.createTask(new Task(4, TaskType.TASK, "Задача 1", Status.NEW, "Описание 1"));
        Task task2 = taskManager.createTask(new Task(5, TaskType.TASK, "Задача 2", Status.NEW, "Описание 2"));

        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "История должна содержать 2 задачи");
        assertEquals(task1, history.get(0), "Первая задача в истории должна быть task1");
        assertEquals(task2, history.get(1), "Вторая задача в истории должна быть task2");
    }

    @Test
    public void testHistoryLimit() {
        for (int i = 0; i < 12; i++) {
            taskManager.createTask(new Task(i + 6, TaskType.TASK, "Задача " + i, Status.NEW, "Описание " + i));
        }

        for (int i = 0; i < 12; i++) {
            taskManager.getTask(i + 6);
        }

        List<Task> history = taskManager.getHistory();
        assertEquals(10, history.size(), "История должна содержать только последние 10 задач");
    }

    @Test
    public void testRemoveTask() {
        Task task = taskManager.createTask(new Task(7, TaskType.TASK, "Задача на удаление", Status.NEW, "Описание"));
        taskManager.deleteTask(task.getId());

        assertNull(taskManager.getTask(task.getId()), "Задача должна быть удалена");
    }

    @Test
    public void testRemoveSubtaskUpdatesEpic() {
        Epic epic = new Epic(8, TaskType.EPIC, "Эпик для подзадачи", Status.NEW, "Описание");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(9, TaskType.SUBTASK, "Подзадача на удаление", Status.NEW, "Описание", epic.getId());
        taskManager.createSubtask(subtask);

        taskManager.deleteSubtask(subtask.getId());

        assertTrue(epic.getSubtask().isEmpty(), "Эпик не должен иметь подзадачи после удаления");
    }

    @Test
    public void testUpdateTask() {
        Task task = taskManager.createTask(new Task(10, TaskType.TASK, "Оригинальная задача", Status.NEW, "Описание"));
        task.setDescription("Обновленное описание");
        taskManager.updateTask(task);

        assertEquals("Обновленное описание", taskManager.getTask(task.getId()).getDescription(), "Описание должно быть обновлено");
    }

    @Test
    public void testIntegrityAfterSubtaskRemoval() {
        Epic epic = new Epic(11, TaskType.EPIC, "Название эпика", Status.NEW, "Описание эпика");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(12, TaskType.SUBTASK, "Подзадача", Status.NEW, "Описание подзадачи", epic.getId());
        taskManager.createSubtask(subtask);

        taskManager.deleteSubtask(subtask.getId());

        assertTrue(epic.getSubtask().isEmpty(), "Эпик должен не иметь подзадач");
    }

    @Test
    public void testRemoveNonExistentTask() {
        taskManager.deleteTask(999);
        assertEquals(0, taskManager.getAllTasks().size(), "Не должно оставаться задач");
    }

    @Test
    public void testRemoveNonExistentSubtask() {
        Epic epic = taskManager.createEpic(new Epic(13, TaskType.EPIC, "Эпик имя", "Эпик описание"));
        taskManager.deleteSubtask(999);
        assertEquals(0, epic.getSubtask().size(), "Эпик должен по-прежнему не иметь подзадач");
    }
}