
public class Task {

    private int id;
    private String name;
    private String description;
    private Status status;

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object obj) {
       if (this == obj) return  true;
       if (obj == null || getClass() != obj.getClass()) return  false;
       Task task = (Task) obj;
       return id == task.id;
     }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }


}

