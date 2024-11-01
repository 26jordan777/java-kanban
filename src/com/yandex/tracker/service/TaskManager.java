package service;

import model.Epic;
import model.Subtask;
import model.Task;

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
        epic.updateStatus();
    }

    public void updateEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
        newEpic.updateStatus();
    }

    public void deletedTasks() {
        tasks.clear();
    }

    public void deletedSubtasks() {
        subtasks.clear();
    }

    public void deletedEpics() {
        for (Epic epic : epics.values()){
            for (Subtask subtask : epic.getSubtask()){
                subtasks.remove(subtask.getId());
            }
        }
        epics.clear();
    }

    public List<Task> getAllTasks(){
        return new ArrayList<>(this.tasks.values());
    }


    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }


    public List<Epic> getAllEpics() {
        return new ArrayList<>(this.epics.values());
    }
}

