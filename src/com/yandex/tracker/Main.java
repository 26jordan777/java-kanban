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
        File file = new File("tasks.csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        if (file.exists()) {
            taskManager = FileBackedTaskManager.loadFromFile(file);
        }

        Task task1 = taskManager.createTask(new Task("Найти университет", "Подать документы в приемную комиссию"));
        Task task2 = taskManager.createTask(new Task("Купить продукты", "Купить сыр, молоко и хлеб"));

        Epic epic1 = taskManager.createEpic(new Epic("Поступление в университет", "Подготовиться к поступлению"));

        Subtask subtask1 = taskManager.createSubtask(new Subtask(0, "Собрать все необходимые документы", "Собрать все документы в папку", epic1.getId()));
        Subtask subtask2 = taskManager.createSubtask(new Subtask(0, "Проверить документы", "Убедиться в их корректности", epic1.getId()));

        Epic epic2 = taskManager.createEpic(new Epic("Найти работу", "Начать поиск новой работы"));
        Subtask subtask3 = taskManager.createSubtask(new Subtask(0, "Обновить резюме", "Добавить новую информацию в резюме", epic2.getId()));

        taskManager.getTask(task1.getId());
        System.out.println("История после получения задачи 1: " + taskManager.getHistory());
        taskManager.getSubtask(subtask1.getId());
        System.out.println("История после получения подзадачи 1: " + taskManager.getHistory());
        taskManager.getEpic(epic1.getId());
        System.out.println("История после получения эпика 1: " + taskManager.getHistory());

        printAllTasks(taskManager);
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