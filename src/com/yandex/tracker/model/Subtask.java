package com.yandex.tracker.model;

public class Subtask extends Task {

    private int epicId;

    public Subtask(int id, String name, String description, int epicId){
        super(id, name, description);
        this.epicId = epicId;
    }
    public int getEpicId() {
        return epicId;
    }
}
