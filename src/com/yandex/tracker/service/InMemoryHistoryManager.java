package com.yandex.tracker.service;

import com.yandex.tracker.model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        history.remove(task); // Удаляем, если задача уже есть в истории
        history.add(task); // Добавляем в конец
        if (history.size() > 10) { // Ограничение на 10 элементов
            history.remove(0); // Удаляем самый старый элемент
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history); // Возвращаем копию истории
    }
}