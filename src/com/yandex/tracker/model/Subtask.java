package com.yandex.tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, TaskType type, String name, Status status, String description, int epicId,Duration duration,LocalDateTime startTime) {
        super(id, type, name, status, description,duration,startTime);
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