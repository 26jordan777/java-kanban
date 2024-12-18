package com.yandex.tracker.model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, TaskType type, String name, Status status, String description, int epicId) {
        super(id, type, name, status, description);
        this.epicId = epicId;
        setId(id);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,SUBTASK,%s,%s,%s,%d",
                getId(), getName(), getStatus(), getDescription(), epicId);
    }

}