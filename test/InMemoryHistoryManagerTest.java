package test;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");

        historyManager.add(task1);
        historyManager.add(task2);

       List<Task> tasks = historyManager.getHistory();
       assertEquals(2, tasks.size(), "History should contain 2 tasks");
    }

    @Test
    public void testGetHistoryEmpty() {
        assertTrue(historyManager.getHistory().isEmpty());
    }

}
