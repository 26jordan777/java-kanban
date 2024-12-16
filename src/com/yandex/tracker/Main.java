package com.yandex.tracker;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.FileBackedTaskManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File("tasks.csv");
        FileBackedTaskManager manager;

        if (file.exists()) {
            manager = FileBackedTaskManager.loadFromFile(file);
        } else {
            manager = new FileBackedTaskManager(file);
        }


        Task task1 = new Task("Купить продукты", "Купить сыр, молоко и хлеб");
        manager.addTask(task1);

        Epic epic1 = new Epic("Поступление в университет", "Подготовиться к поступлению");
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask(1, "Собрать все необходимые документы", "Собрать все документы в папку", epic1.getId());
        Subtask subtask2 = new Subtask(2, "Проверить документы", "Убедиться в их корректности", epic1.getId());

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        Epic epic2 = new Epic("Найти работу", "Начать поиск новой работы");
        manager.addEpic(epic2);

        Subtask subtask3 = new Subtask(3, "Обновить резюме", "Добавить новую информацию в резюме", epic2.getId());
        manager.addSubtask(subtask3);

        manager.save();

        manager.getTask(task1.getId());
        System.out.println("История после получения задачи 1: " + manager.getHistory());
        manager.getSubtask(subtask1.getId());
        System.out.println("История после получения подзадачи 1: " + manager.getHistory());
        manager.getEpic(epic1.getId());
        System.out.println("История после получения эпика 1: " + manager.getHistory());

        printAllTasks(manager);
    }

    private static void printAllTasks(FileBackedTaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
            for (Subtask subtask : epic.getSubtask()) {
                System.out.println("--> " + subtask);
            }
        }
        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}