package com.yandex.tracker.test;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @Test
    public void testSaveAndLoad() throws Exception {
        File tempFile = File.createTempFile("taskManager", ".csv");
        tempFile.deleteOnExit();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);
        taskManager.createTask(new Task("Task1", "Description task1"));
        taskManager.createEpic(new Epic("Epic1", "Description epic1"));
        taskManager.createSubtask(new Subtask(1, "Subtask1", "Description subtask1", 2));


        taskManager.save();

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(tempFile);

        assertEquals(1, loadedManager.getAllTasks().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtasks().size());
    }

    @Test
    public void testLoadEmptyFile() throws Exception {
        File tempFile = File.createTempFile("emptyTaskManager", ".csv");
        tempFile.deleteOnExit();

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(tempFile);

        assertEquals(0, loadedManager.getAllTasks().size());
        assertEquals(0, loadedManager.getAllEpics().size());
        assertEquals(0, loadedManager.getAllSubtasks().size());
    }
}