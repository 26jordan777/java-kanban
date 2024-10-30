import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Subtask> subtasks;

    public Epic(int id, String name, String description){
        super(id, name, description);
        this.subtasks = new ArrayList<>();
    }
    public void addSubtask(Subtask subtask){
        subtasks.add(subtask);
    }

    public ArrayList<Subtask> getSubtask(){
        return subtasks;
    }

    public void updateStatus(){
        if (subtasks.isEmpty()){
           setStatus(Status.NEW);
        }else {
            setStatus(Status.IN_PROGRESS);
        }
    }
}

