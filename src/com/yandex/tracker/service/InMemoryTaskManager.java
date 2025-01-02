package com.yandex.tracker.service;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int tasksId = 0;
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public void addTask(Task task) {
        if (hasOverlappingTasks(task)) {
            throw new IllegalArgumentException("Task overlaps with an existing task.");
        }
        prioritizedTasks.add(task);

    }

    public boolean hasOverlappingTasks(Task newTask) {
        List<Task> tasks = new ArrayList<>(prioritizedTasks);
        for (Task existingTask : tasks) {
            if (isOverlapping(existingTask, newTask)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOverlapping(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return (start1 != null && end1 != null && start2 != null && end2 != null) &&
                (start1.isBefore(end2) && start2.isBefore(end1));
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }



    @Override
    public Task createTask(Task task) {
        task.setId(tasksId++);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(tasksId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
            epic.updateStatus();
        }
        return subtask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(tasksId++);
        epics.put(epic.getId(), epic);
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
        Task task = tasks.remove(id);
        if (task != null) {
            historyManager.remove(id);
        }
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
                historyManager.remove(subtask.getId());
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deletedAllTasks() {
        for (Integer id : new ArrayList<>(tasks.keySet())) {
            deletedTask(id);
        }
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
        for (Integer id : new ArrayList<>(epics.keySet())) {
            deletedEpic(id);
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

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}