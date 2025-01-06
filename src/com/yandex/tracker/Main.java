package com.yandex.tracker;

import com.yandex.tracker.model.Epic;
import com.yandex.tracker.model.Subtask;
import com.yandex.tracker.model.Task;
import com.yandex.tracker.model.Status;
import com.yandex.tracker.model.TaskType;
import com.yandex.tracker.service.Managers;
import com.yandex.tracker.service.TaskManager;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = taskManager.createTask(new Task(1, TaskType.TASK, "Найти университет", Status.NEW, "Подать документы в приемную комиссию", Duration.ofMinutes(30),LocalDateTime.now()));
        Task task2 = taskManager.createTask(new Task(2, TaskType.TASK, "Купить продукты", Status.NEW, "Купить сыр, молоко и хлеб", Duration.ofMinutes(15), LocalDateTime.now().plusHours(1)));

        Epic epic1 = taskManager.createEpic(new Epic(3, TaskType.EPIC, "Поступление в университет", Status.NEW, "Подготовиться к поступлению"));

        Subtask subtask1 = taskManager.createSubtask(new Subtask(4, TaskType.SUBTASK, "Собрать все необходимые документы", Status.NEW, "Собрать все документы в папку", epic1.getId(),Duration.ofMinutes(30), LocalDateTime.now().plusHours(5)));
        Subtask subtask2 = taskManager.createSubtask(new Subtask(5, TaskType.SUBTASK, "Проверить документы", Status.NEW, "Убедиться в их корректности", epic1.getId(),Duration.ofMinutes(30),LocalDateTime.now().plusMinutes(80)));

        Epic epic2 = taskManager.createEpic(new Epic(6, TaskType.EPIC, "Найти работу", Status.NEW, "Начать поиск новой работы"));
        Subtask subtask3 = taskManager.createSubtask(new Subtask(7, TaskType.SUBTASK, "Обновить резюме", Status.NEW, "Добавить новую информацию в резюме", epic2.getId(),Duration.ofMinutes(60),LocalDateTime.now().plusHours(2)));

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
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Subtask subtask : manager.getAllSubtasks()) {
                if (subtask.getEpicId() == epic.getId()) {
                    System.out.println("--> " + subtask);
                }
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}