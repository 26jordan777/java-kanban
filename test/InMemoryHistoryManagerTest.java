package test;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.InMemoryHistoryManager;
import com.yandex.tracker.model.TaskType;
import com.yandex.tracker.model.Status;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    @Test
    void testGetHistoryEmpty() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        assertTrue(historyManager.getHistory().isEmpty(), "History should be empty initially");
    }

    @Test
    void testAddTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task(1, TaskType.TASK, "Task", Status.NEW, "Task description",
                Duration.ofMinutes(15), LocalDateTime.now());

        historyManager.add(task);
        List<Task> tasks = historyManager.getHistory();
        assertEquals(1, tasks.size(), "History should contain 1 task");
        assertEquals(task, tasks.get(0), "The task in history should be the same as the added task");
    }

    @Test
    void testRemoveTaskFromHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task(1, TaskType.TASK, "Task", Status.NEW, "Task description",
                Duration.ofMinutes(15), LocalDateTime.now());

        historyManager.add(task);
        historyManager.remove(task.getId());
        assertTrue(historyManager.getHistory().isEmpty(), "History should be empty after removing the task");
    }
}