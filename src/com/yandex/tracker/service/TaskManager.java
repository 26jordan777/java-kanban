package com.yandex.tracker.service;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;

import java.awt.image.SinglePixelPackedSampleModel;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int tasksId = 0;

    public Task createTask(Task task) {
        final int id = tasksId++;
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

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

    public Epic createEpic(Epic epic) {
        final int id = tasksId++;
        epic.setId(id);
        epics.put(id, epic);
        return epic;
    }

    public void updateTask(Task newTask) {
        tasks.put(newTask.getId(), newTask);
    }

    public void updateSubtask(Subtask newSubtask) {
        Epic epic = epics.get(newSubtask.getEpicId());
        if (epic == null) {
            return;
        }
        subtasks.put(newSubtask.getId(), newSubtask);
        epic.getSubtask().removeIf(subtask -> subtask.getId() == newSubtask.getId());
        subtasks.put(newSubtask.getId(), newSubtask);
        epic.addSubtask(newSubtask);
        epic.updateStatus();
    }

    public void updateEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
    }

    public void deletedTask(int id) {
        tasks.remove(id);
    }

    public void deletedSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if ((epic != null)) {
                epic.getSubtask().remove(subtask);
                epic.updateStatus();
            }
        }
    }

    public void deletedEpic(int id) {
        for (Epic epic : epics.values()) {
            for (Subtask subtask : epic.getSubtask()) {
                subtasks.remove(subtask.getId());
            }
        }
        epics.remove(id);
    }

    public void deletedAllTasks() {
        tasks.clear();
    }

    public void deletedAllSubtasks() {
        for (Epic epic : epics.values()){
            for (Subtask subtask : epic.getSubtask()){
                subtasks.remove(subtask.getId());
            }
            epic.getSubtask().clear();
        }
        subtasks.clear();
    }

    public void deletedAllEpics() {
        for (Epic epic : epics.values()){
            for (Subtask subtask : epic.getSubtask()){
                subtasks.remove(subtask.getId());
            }
        }
        epics.clear();
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(this.tasks.values());
    }


    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }


    public List<Epic> getAllEpics() {
        return new ArrayList<>(this.epics.values());
    }

public List<Subtask> getEpicSubtasks(int epicId){
        Epic epic = epics.get(epicId);
        return epic.getSubtask();
}

    public Task getTask(int id){
        return  tasks.get(id);
    }

    public Subtask getSubtask(int id){
        return subtasks.get(id);
    }

    public Epic getEpic(int id){
        return epics.get(id);
    }
}

