package com.yandex.tracker.service;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;


import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
        loadFromFile();
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();
            for (Task task : getAllTasks()) {
                writer.write(task.toString());
                writer.newLine();
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write(subtask.toString());
                writer.newLine();
            }
            for (Epic epic : getAllEpics()) {
                writer.write(epic.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении задач", e);
        }
    }

    private void loadFromFile() {
        if (!file.exists()) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            
            if (lines.size() <= 1) {
                return;
            }
            for (String line : lines.subList(1, lines.size())) {
                if (line.startsWith("TASK")) {
                    Task task = Task.fromString(line);
                    createTask(task);
                } else if (line.startsWith("SUBTASK")) {
                    Subtask subtask = Subtask.fromString(line);
                    createSubtask(subtask);
                } else if (line.startsWith("EPIC")) {
                    Epic epic = Epic.fromString(line);
                    createEpic(epic);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке задач", e);
        }
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        save();
        return createdSubtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public void deletedTask(int id) {
        super.deletedTask(id);
        save();
    }

    @Override
    public void deletedSubtask(int id) {
        super.deletedSubtask(id);
        save();
    }

    @Override
    public void deletedEpic(int id) {
        super.deletedEpic(id);
        save();
    }
}