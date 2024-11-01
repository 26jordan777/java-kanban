package com.yandex.tracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description) {
        super(0, name, description);
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtask() {
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
}

