package test;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
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
        Task task = new Task("Test Task", "Test Description");
        taskManager.createTask(task);

        Task retrievedTask = taskManager.getTask(task.getId());
        assertEquals(task, retrievedTask);
    }

    @Test
    public void testCreateAndGetSubtask() {
        Epic epic = taskManager.createEpic(new Epic("Epic Name", "Epic Description"));
        Subtask subtask = new Subtask(0, "Test Subtask", "Test Subtask Description", epic.getId());
        taskManager.createSubtask(subtask);

        Subtask retrievedSubtask = taskManager.getSubtask(subtask.getId());
        assertEquals(subtask, retrievedSubtask);
    }

    @Test
    public void testHistory() {
        Task task1 = taskManager.createTask(new Task("Task 1", "Description 1"));
        Task task2 = taskManager.createTask(new Task("Task 2", "Description 2"));

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
            taskManager.createTask(new Task("Task " + i, "Description " + i));
        }

        for (int i = 0; i < 12; i++) {
            taskManager.getTask(i);
        }

        List<Task> history = taskManager.getHistory();
        assertEquals(10, history.size(), "History should contain only the last 10 tasks");
    }

    @Test
    public void testRemoveTask() {
        Task task = taskManager.createTask(new Task("Task to Remove", "Description"));
        taskManager.deletedTask(task.getId());

        assertNull(taskManager.getTask(task.getId()), "Task should be removed");
    }

    @Test
    public void testRemoveSubtaskUpdatesEpic() {
        Epic epic = taskManager.createEpic(new Epic("Epic for Subtask", "Description"));
        Subtask subtask = new Subtask(0, "Subtask to Remove", "Description", epic.getId());
        taskManager.createSubtask(subtask);

        taskManager.deletedSubtask(subtask.getId());

        assertTrue(epic.getSubtask().isEmpty(), "Epic should not have subtasks after removal");
    }

    @Test
    public void testUpdateTask() {
        Task task = taskManager.createTask(new Task("Original Task", "Description"));
        task.setDescription("Updated Description");
        taskManager.updateTask(task);

        assertEquals("Updated Description", taskManager.getTask(task.getId()).getDescription());
    }

    @Test
    public void testIntegrityAfterSubtaskRemoval() {
        Epic epic = taskManager.createEpic(new Epic("Epic Name", "Epic Description"));
        Subtask subtask = new Subtask(0, "Subtask", "Subtask Description", epic.getId());
        taskManager.createSubtask(subtask);

        taskManager.deletedSubtask(subtask.getId());

        assertTrue(epic.getSubtask().isEmpty(), "Epic should have no subtasks");
    }

    @Test
    public void testRemoveNonExistentTask() {
        taskManager.deletedTask(999);
        assertEquals(0, taskManager.getAllTasks().size(), "No tasks should remain");
    }

    @Test
    public void testRemoveNonExistentSubtask() {
        Epic epic = taskManager.createEpic(new Epic("Epic Name", "Epic Description"));
        taskManager.deletedSubtask(999);
        assertEquals(0, epic.getSubtask().size(), "Epic should still have no subtasks");
    }
}