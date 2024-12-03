package test;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    public void setuo() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void testAddTask() {
        Task task1 = new Task("Task 1", "Description 1");
        historyManager.add(task1);

        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
    }

    @Test
    public void testAddMultipleTasks() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");

        historyManager.add(task1);
        historyManager.add(task2);

        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
        assertEquals(task2, historyManager.getHistory().get(1));
    }

    @Test
    public void testGetHistoryEmpty() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

}
