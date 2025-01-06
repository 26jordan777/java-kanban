package com.yandex.tracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(int id, TaskType type, String name, Status status, String description) {
        super(id, type, name, status, description, Duration.ZERO, null);
        this.subtasks = new ArrayList<>();
        this.duration = Duration.ZERO;
        this.startTime = null;
        this.endTime = null;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateEpicDetails();
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
        return String.format("%d,EPIC,%s,%s,%s,", getId(), getName(), getStatus(), getDescription());
    }

    public void updateEpicDetails() {
        if (subtasks.isEmpty()) {
            this.duration = Duration.ZERO;
            this.startTime = null;
            this.endTime = null;
        } else {
            this.duration = subtasks.stream()
                    .map(Subtask::getDuration)
                    .reduce(Duration.ZERO, Duration::plus);
            this.startTime = subtasks.stream()
                    .map(Subtask::getStartTime)
                    .filter(start -> start != null)
                    .min(LocalDateTime::compareTo)
                    .orElse(null);
            this.endTime = subtasks.stream()
                    .map(Subtask::getEndTime)
                    .filter(end -> end != null)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}