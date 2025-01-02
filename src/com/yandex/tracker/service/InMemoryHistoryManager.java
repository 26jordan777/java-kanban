package com.yandex.tracker.service;

import com.yandex.tracker.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> historyMap = new HashMap<>();
    private Node head;
    private Node tail;
    private final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {

        if (!history.contains(task)) {
            history.add(task);
        }

        if (historyMap.containsKey(task.getId())) {
            removeNode(historyMap.get(task.getId()));
        }

        Node newNode = new Node(task);
        linkLast(newNode);
        historyMap.put(task.getId(), newNode);

        if (historyMap.size() > 10) {
            removeNode(head);
        }

    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
        }
        history.removeIf(task -> task.getId() == id);
    }

    @Override
    public List<Task> getHistory() {

        return new ArrayList<>(history);
    }



    private void linkLast(Node newNode) {
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        historyMap.remove(node.task.getId());
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }


}