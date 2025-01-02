package com.yandex.tracker.test;

import com.yandex.tracker.model.*;
import com.yandex.tracker.service.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @Test
    public void testSaveAndLoad() throws Exception {
        File tempFile = File.createTempFile("taskManager", ".csv");
        tempFile.deleteOnExit();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);

        taskManager.createTask(new Task(0, TaskType.TASK, "Task1", Status.NEW, "Description task1",Duration.ofMinutes(30), LocalDateTime.now()));
        taskManager.createEpic(new Epic(0, TaskType.EPIC, "Epic1", Status.NEW, "Description epic1"));
        taskManager.createSubtask(new Subtask(1, TaskType.SUBTASK, "Subtask1", Status.NEW, "Description subtask1", 2, Duration.ofMinutes(20), LocalDateTime.now()));

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