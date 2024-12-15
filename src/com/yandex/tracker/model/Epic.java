package com.yandex.tracker.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public List<Subtask> getSubtask() {
        return subtasks;
    }

    public void updateStatus() {
        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);
        } else if (subtasks.stream().allMatch(subtask -> subtask.getStatus() == Status.DONE)) {
            setStatus(Status.DONE);

        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "Epic{" + "id=" + getId() + ", name='" + getName() + '\'' + ", description='" + getDescription() + '\'' + ", status=" + getStatus() + ", subtasksCount=" + subtasks.size() + '}';
    }
}

