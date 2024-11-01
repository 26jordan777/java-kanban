import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = taskManager.createTask(new Task(0,"Найти университет", "Подать документы в приемную комиссию"));
        Task task2 = taskManager.createTask(new Task(0,"Купить продукты", "Купить сыр, молоко и хлеб"));


        Epic epic1 = taskManager.createEpic(new Epic( "Поступление в университет", "Подготовиться к поступлению"));

        Subtask subtask1 = taskManager.createSubtask(new Subtask(0,"Собрать все необходимые документы",
                "Собрать все документы в папку", epic1.getId()));
        Subtask subtask2 = taskManager.createSubtask(new Subtask(0,"Проверить документы",
                "Убедиться в их корректности", epic1.getId()));

        Epic epic2 = taskManager.createEpic(new Epic("Найти работу", "Начать поиск новой работы"));
        Subtask subtask3 = taskManager.createSubtask(new Subtask(0,"Обновить резюме", "Добавить новую информацию " +
                "в резюме", epic2.getId()));

        System.out.println();
        System.out.println("Все задачи:");
        for (Task task : taskManager.getAllTasks()) {
            System.out.println(task.getName() + " Статус: " + task.getStatus());
        }

        System.out.println();
        System.out.println("Все подзадачи:");
        for (Subtask subtask : taskManager.getAllSubtasks()) {
            System.out.println(subtask.getName() + " Эпик ID: " + subtask.getEpicId() +
                    " Статус: " + subtask.getStatus());
        }
        System.out.println();
        System.out.println("Все эпики:");
        for (Epic epic : taskManager.getAllEpics()) {
            System.out.println(epic.getName() + " Статус: " + epic.getStatus());
        }


        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        System.out.println();
        System.out.println("Статус после изменений: ");
        System.out.println("Задача 1 " + task1.getName() + " Статус - " + task1.getStatus());
        System.out.println("Задача 2 " + task2.getName() + " Статус - " + task2.getStatus());

        epic1.updateStatus();
        epic2.updateStatus();

        System.out.println("Эпик 1 " + epic1.getName() + " Статус :" + epic1.getStatus());
        System.out.println("Эпик 2 " + epic2.getName() + " Статус :" + epic2.getStatus());


        taskManager.deletedTasks(task2.getId());
        taskManager.deletedEpics(epic2.getId());

        System.out.println();
        System.out.println("После удаления задачи и эпика:");
        System.out.println("Оставшиеся задачи:");
        for (Epic epic : taskManager.getAllEpics()) {
            epic.updateStatus();
            System.out.println(epic.getName() + " Статус: " + epic.getStatus());
        }
    }
}
