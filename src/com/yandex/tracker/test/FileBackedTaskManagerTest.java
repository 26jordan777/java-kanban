package com.yandex.tracker.test;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @Test
    public void testSaveAndLoad() throws IOException {
        File tempFile = File.createTempFile("taskManager", ".csv");
        tempFile.deleteOnExit();
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        manager.createTask(new Task("Task 1", "Description task1"));
        manager.createEpic(new Epic("Epic 1", "Description epic1"));
        manager.createSubtask(new Subtask(1, "Subtask 1", "Description subtask1", 2));

        manager.save();


        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(1, loadedManager.getTasks().size());
        assertEquals(1, loadedManager.getEpics().size());
        assertEquals(1, loadedManager.getSubtasks().size());
    }

    @Test
    public void testLoadEmptyFile() throws IOException {
        File tempFile = File.createTempFile("emptyTaskManager", ".csv");
        tempFile.deleteOnExit();

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(0, loadedManager.getTasks().size());
        assertEquals(0, loadedManager.getEpics().size());
        assertEquals(0, loadedManager.getSubtasks().size());
    }
}