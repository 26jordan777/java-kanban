package com.yandex.tracker.test;

import com.yandex.tracker.model.*;
import com.yandex.tracker.service.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest  {

    @Test
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(new File("tasks.csv"));
    }

    @Test
    public void testSaveAndLoad() throws Exception {
        File tempFile = File.createTempFile("taskManager", ".csv");
        tempFile.deleteOnExit();

        FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);

        taskManager.createTask(new Task(1, TaskType.TASK, "Task1", Status.NEW, "Description task1",
                Duration.ofMinutes(30), LocalDateTime.now()));

        taskManager.createEpic(new Epic(2, TaskType.EPIC, "Epic1", Status.NEW, "Description epic1"));


        taskManager.createSubtask(new Subtask(3, TaskType.SUBTASK, "Subtask1", Status.NEW,
                "Description subtask1", epic.getId(),
                Duration.ofMinutes(20), LocalDateTime.now()));

        taskManager.save();

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(tempFile);

        assertEquals(1, loadedManager.getAllTasks().size(), "Должна быть 1 задача после загрузки");
        assertEquals(1, loadedManager.getAllEpics().size(), "Должен быть 1 эпик после загрузки");
        assertEquals(1, loadedManager.getAllSubtasks().size(), "Должна быть 1 подзадача после загрузки");
    }

    @Test
    public void testLoadEmptyFile() throws Exception {
        File tempFile = File.createTempFile("emptyTaskManager", ".csv");
        tempFile.deleteOnExit();

        FileBackedTaskManager loadedManager = new FileBackedTaskManager(tempFile);
        assertEquals(0, loadedManager.getAllTasks().size(), "Должно быть 0 задач после загрузки пустого файла");
        assertEquals(0, loadedManager.getAllEpics().size(), "Должно быть 0 эпиков после загрузки пустого файла");
        assertEquals(0, loadedManager.getAllSubtasks().size(), "Должно быть 0 подзадач после загрузки пустого файла");
    }
}