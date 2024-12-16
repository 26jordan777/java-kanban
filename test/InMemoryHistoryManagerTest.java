package test;

import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;
    private Task task1;
    private Task task2;


    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Description for task 1");
        task2 = new Task("Task 2", "Description for task 2");
    }


    @Test
    void testGetHistoryEmpty() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        assertTrue(historyManager.getHistory().isEmpty(), "Изначально история должна быть пустой");
    }

    @Test
    void testAddTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task", "Task 1");
        historyManager.add(task);

        List<Task> tasks = historyManager.getHistory();
        assertEquals(1, tasks.size(), "История должна содержать 1 историю");
        assertEquals(task, tasks.get(0), "История задачи должна совпадать  добавленной задачей");
    }
}
