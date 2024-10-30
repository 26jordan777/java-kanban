import java.util.HashMap;


public class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private int tasksId = 0;

    public Task createTask(String name, String description) {
        tasksId++;
        Task task = new Task(tasksId, name, description);
        tasks.put(tasksId, task);
        return task;
    }

    public Subtask createSubtask(String name, String description, int epicId) {
        tasksId++;
        Subtask subtask = new Subtask(tasksId, name, description, epicId);
        subtasks.put(tasksId, subtask);
        Epic epic = epics.get(epicId);
        if (epic != null) {
            epic.addSubtask(subtask);
            epic.updateStatus();
        }
        return subtask;
    }

    public Epic createEpic(String name, String description) {
        tasksId++;
        Epic epic = new Epic(tasksId, name, description);
        epics.put(tasksId, epic);
        return epic;
    }

    public void updateTask(Task newTask) {
        tasks.put(newTask.getId(), newTask);
    }

    public void updateSubtask(Subtask newSubtask) {
        subtasks.put(newSubtask.getId(), newSubtask);
        Epic epic = epics.get(newSubtask.getEpicId());
        if (epic != null) {
            epic.updateStatus();
        }
    }

    public void updateEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
        newEpic.updateStatus();
    }

    public void deletedTask(int id) {
        tasks.remove(id);
    }

    public void deletedSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if ((epic != null)) {
                epic.getSubtask().remove(subtask);
                epic.updateStatus();
            }
        }
    }

    public void deletedEpic(int id) {
        epics.remove(id);
    }

    public HashMap<Integer, Task> getAllTasks() {
        return tasks;
    }


    public HashMap<Integer, Subtask> getAllSubtasks() {
        return subtasks;
    }


    public HashMap<Integer, Epic> getAllEpics() {
        return epics;
    }
}

