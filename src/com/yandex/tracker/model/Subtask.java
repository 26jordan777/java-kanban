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
        return String.format("%d,SUBTASK,%s,%s,%s,%d",
                getId(), getName(), getStatus(), getDescription(), epicId);
    }

    public static Subtask fromString(String value) {
        String[] fields = value.split(",");
        Subtask subtask = new Subtask(Integer.parseInt(fields[0]), fields[2], fields[4], Integer.parseInt(fields[5]));
        subtask.setStatus(Status.valueOf(fields[3]));
        return subtask;
    }
}