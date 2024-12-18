package com.yandex.tracker.model;

public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;
    private TaskType type;


    public Task(int id, TaskType type, String name, Status status, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("%d,TASK,%s,%s,%s,", id, name, status, description);
    }
}