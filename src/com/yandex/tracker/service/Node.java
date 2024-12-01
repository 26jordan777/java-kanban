package com.yandex.tracker.service;

import com.yandex.tracker.model.Task;

public class Node {
    Task task;
    Node prev;
    Node next;

    public Node(Task task) {
        this.task = task;
    }
}