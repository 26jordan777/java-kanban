package com.yandex.tracker;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager manager;
    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {

        tempFile = File.createTempFile("tasks", ".csv");
        tempFile.deleteOnExit();
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    public void tearDown() {

        tempFile.delete();
    }

    @Test
    public void testSaveAndLoadEmptyFile() {

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(0, loadedManager.getAllTasks().size());
        assertEquals(0, loadedManager.getAllEpics().size());
        assertEquals(0, loadedManager.getAllSubtasks().size());
    }

    @Test
    public void testSaveAndLoadMultipleTasks() {
        Task task1 = new Task("Task 1", "Description for task 1");
        Task task2 = new Task("Task 2", "Description for task 2");
        Epic epic = new Epic("Epic 1", "Description for epic 1");
        Subtask subtask1 = new Subtask(0, "Subtask 1", "Description for subtask 1", epic.getId());
        Subtask subtask2 = new Subtask(1, "Subtask 2", "Description for subtask 2", epic.getId());


        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);


        manager.save();


        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);


        assertEquals(2, loadedManager.getAllTasks().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(2, loadedManager.getAllSubtasks().size());
    }

    @Test
    public void testLoadFromFile() {
        Task task1 = new Task("Task 1", "Description for task 1");
        Task task2 = new Task("Task 2", "Description for task 2");

        manager.addTask(task1);
        manager.addTask(task2);
        manager.save();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(2, loadedManager.getAllTasks().size());
    }
}