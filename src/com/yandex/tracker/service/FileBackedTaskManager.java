package com.yandex.tracker.service;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.TaskType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }


    public void save() {
        try {
            List<String> taskLines = new ArrayList<>();

            taskLines.add("id,type,name,status,description,epic");


            for (Task task : getAllTasks()) {
                taskLines.add(taskToString(task));
            }


            for (Epic epic : getAllEpics()) {
                taskLines.add(epicToString(epic));
                for (Subtask subtask : epic.getSubtask()) {
                    taskLines.add(subtaskToString(subtask));
                }
            }
            Files.write(file.toPath(), taskLines);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения задач", e);
        }
    }

    private String taskToString(Task task) {
        return String.format("%d,%s,%s,%s,%s,",
                task.getId(), TaskType.TASK, task.getName(), task.getStatus(), task.getDescription());
    }

    private String epicToString(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,",
                epic.getId(), TaskType.EPIC, epic.getName(), epic.getStatus(), epic.getDescription());
    }

    private String subtaskToString(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%d",
                subtask.getId(), TaskType.SUBTASK, subtask.getName(), subtask.getStatus(), subtask.getEpicId());
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }


    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.size() > 1) {
                for (String line : lines.subList(1, lines.size())) {
                    String[] parts = line.split(",");
                    if (parts.length < 5) continue;
                    TaskType type = TaskType.valueOf(parts[1]);
                    switch (type) {
                        case TASK:
                            Task task = new Task(parts[2], parts[4]);
                            task.setId(Integer.parseInt(parts[0]));
                            manager.addTask(task);
                            break;
                        case EPIC:
                            Epic epic = new Epic(parts[2], parts[4]);
                            epic.setId(Integer.parseInt(parts[0]));
                            manager.addEpic(epic);
                            break;
                        case SUBTASK:
                            if (parts.length < 6) continue;
                            Subtask subtask = new Subtask(Integer.parseInt(parts[0]), parts[2], parts[4], Integer.parseInt(parts[5]));
                            manager.addSubtask(subtask);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка загрузки задач", e);
        }
        return manager;
    }
}