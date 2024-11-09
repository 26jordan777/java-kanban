package com.yandex.tracker.service;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory(); // Используем HistoryManager
    private int tasksId = 0;

    @Override
    public Task createTask(Task task) {
        final int id = tasksId++;
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        final int id = tasksId++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
            epic.updateStatus();
        }
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        final int id = tasksId++;
        epic.setId(id);
        epics.put(id, epic);
        return epic;
    }

    @Override
    public void updateTask(Task newTask) {
        tasks.put(newTask.getId(), newTask);
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        Epic epic = epics.get(newSubtask.getEpicId());
        if (epic != null) {
            subtasks.put(newSubtask.getId(), newSubtask);
            epic.getSubtask().removeIf(subtask -> subtask.getId() == newSubtask.getId());
            epic.addSubtask(newSubtask);
            epic.updateStatus();
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
    }

    @Override
    public void deletedTask(int id) {
        tasks.remove(id);
    }

    @Override
    public void deletedSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.getSubtask().remove(subtask);
                epic.updateStatus();
            }
        }
    }

    @Override
    public void deletedEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtask()) {
                subtasks.remove(subtask.getId());
            }
        }
    }

    @Override
    public void deletedAllTasks() {
        tasks.clear();
    }

    @Override
    public void deletedAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtask().clear();
        }
        subtasks.clear();
    }

    @Override
    public void deletedAllEpics() {
        for (Epic epic : epics.values()) {
            epic.getSubtask().clear();
        }
        epics.clear();
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task); // Используем HistoryManager
        }
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask); // Используем HistoryManager
        }
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic); // Используем HistoryManager
        }
        return epic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory(); // Используем HistoryManager
    }
}