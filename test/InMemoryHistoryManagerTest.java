package test;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    @Test
    void testGetHistoryEmpty() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        assertTrue(historyManager.getHistory().isEmpty(), "History should be empty initially");
    }

    @Test
    void testAddMultipleTasks() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task(1, "Task 1");
        Task task2 = new Task(2, "Task 2");

        historyManager.add(task1); // Добавляем первую задачу
        historyManager.add(task2); // Добавляем вторую задачу

        List<Task> tasks = historyManager.getHistory(); // Получаем историю
        assertEquals(2, tasks.size(), "History should contain 2 tasks"); // Проверяем количество задач
    }

    @Test
    void testAddTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task(1, "Task 1");
        historyManager.add(task);

        List<Task> tasks = historyManager.getHistory();
        assertEquals(1, tasks.size(), "History should contain 1 task");
        assertEquals(task, tasks.get(0), "The task in history should be the same as the added task");
    }
}
