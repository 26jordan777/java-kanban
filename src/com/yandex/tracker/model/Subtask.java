package com.yandex.tracker.model;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
        setId(id);

    }

    public int getEpicId() {
        return epicId;

    }

    @Override
    public String toString() {
        return "Subtask{" + "id=" + getId() + ", name='" + getName() + '\'' + ", description='" + getDescription() + '\'' + ", status=" + getStatus() + ", epicId=" + epicId + '}';

    }

}
