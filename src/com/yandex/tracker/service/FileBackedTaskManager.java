package com.yandex.tracker.service;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.Status;
import com.yandex.tracker.model.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
        loadFromFile();
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic,duration,startTime");
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

    public void loadFromFile() {
        if (!file.exists()) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.size() <= 1) {
                return;
            }
            for (String line : lines.subList(1, lines.size())) {
                Task task = taskFromString(line);
                if (task instanceof Subtask) {
                    createSubtask((Subtask) task);
                } else if (task instanceof Epic) {
                    createEpic((Epic) task);
                } else {
                    createTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке задач", e);
        }
    }

    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String description = values[4];

        Duration duration = Duration.ZERO;
        LocalDateTime startTime = null;

        if (values.length > 6) {
            duration = Duration.ofMinutes(Long.parseLong(values[6]));
        }

        if (values.length > 7) {
            startTime = values[7].isEmpty() ? null : LocalDateTime.parse(values[7]);
        }

        if (type == TaskType.TASK) {
            return new Task(id, type, name, status, description, duration, startTime);
        }

        if (type == TaskType.SUBTASK) {
            final int epicId = Integer.parseInt(values[5]);
            return new Subtask(id, type, name, status, description, epicId, duration, startTime);
        }

        return new Epic(id, type, name, status, description);
    }

    public String taskToFileString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s,%d,%s", task.getId(), task.getType(), task.getName(), task.getStatus(), task.getDescription(), (task instanceof Subtask) ? ((Subtask) task).getEpicId() : "", task.getDuration().toMinutes(), task.getStartTime() != null ? task.getStartTime() : "");
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

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }
}