package com.yandex.tracker;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.service.FileBackedTaskManager;
import com.yandex.tracker.service.Managers;
import com.yandex.tracker.service.TaskManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        File file = new File("tasks.csv");

        FileBackedTaskManager fileBackedManager = new FileBackedTaskManager(file);

        if (file.exists()) {
            fileBackedManager = FileBackedTaskManager.loadFromFile(file);
        }

        Task task1 = fileBackedManager.createTask(new Task("Найти университет", "Подать документы в приемную комиссию"));
        Task task2 = fileBackedManager.createTask(new Task("Купить продукты", "Купить сыр, молоко и хлеб"));

        Epic epic1 = fileBackedManager.createEpic(new Epic("Поступление в университет", "Подготовиться к поступлению"));

        Subtask subtask1 = fileBackedManager.createSubtask(new Subtask(0, "Собрать все необходимые документы", "Собрать все документы в папку", epic1.getId()));
        Subtask subtask2 = fileBackedManager.createSubtask(new Subtask(0, "Проверить документы", "Убедиться в их корректности", epic1.getId()));

        Epic epic2 = fileBackedManager.createEpic(new Epic("Найти работу", "Начать поиск новой работы"));
        Subtask subtask3 = fileBackedManager.createSubtask(new Subtask(0, "Обновить резюме", "Добавить новую информацию в резюме", epic2.getId()));

        fileBackedManager.getTask(task1.getId());
        System.out.println("История после получения задачи 1: " + fileBackedManager.getHistory());
        fileBackedManager.getSubtask(subtask1.getId());
        System.out.println("История после получения подзадачи 1: " + fileBackedManager.getHistory());
        fileBackedManager.getEpic(epic1.getId());
        System.out.println("История после получения эпика 1: " + fileBackedManager.getHistory());

        printAllTasks(fileBackedManager);
    }

    private static void printAllTasks(TaskManager manager) {
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