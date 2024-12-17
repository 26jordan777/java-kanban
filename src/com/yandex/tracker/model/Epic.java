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
        return String.format("%d,EPIC,%s,%s,%s,",
                getId(), getName(), getStatus(), getDescription());
    }

    public static Epic fromString(String value) {
        String[] fields = value.split(",");
        Epic epic = new Epic(fields[2], fields[4]);
        epic.setId(Integer.parseInt(fields[0]));
        epic.setStatus(Status.valueOf(fields[3]));
        return epic;
    }
}