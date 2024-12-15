package com.yandex.tracker;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.FileBackedTaskManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File("tasks.csv");


        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        if (file.exists()) {
            manager = FileBackedTaskManager.loadFromFile(file);
        }

        Task task1 = manager.createTask(new Task("Найти университет", "Подать документы в приемную комиссию"));
        Task task2 = manager.createTask(new Task("Купить продукты", "Купить сыр, молоко и хлеб"));

        Epic epic1 = manager.createEpic(new Epic("Поступление в университет", "Подготовиться к поступлению"));

        Subtask subtask1 = manager.createSubtask(new Subtask(0, "Собрать все необходимые документы", "Собрать все документы в папку", epic1.getId()));
        Subtask subtask2 = manager.createSubtask(new Subtask(0, "Проверить документы", "Убедиться в их корректности", epic1.getId()));

        Epic epic2 = manager.createEpic(new Epic("Найти работу", "Начать поиск новой работы"));
        Subtask subtask3 = manager.createSubtask(new Subtask(0, "Обновить резюме", "Добавить новую информацию в резюме", epic2.getId()));

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