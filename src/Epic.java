import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtasks;


    public Epic(String title, String description) {
        // конструктор родителя Task, который устанавливает параметры и статус
        super(title, description, Status.NEW);
        subtasks = new HashMap<>(); // создаем коллекцию для подзадач, ссылку на коллекцию храним в subtasks
    }

    public HashMap<Integer, Subtask> getAllSubtask() {
        return subtasks;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (!super.equals(object)) return false;
        if (!Objects.equals(this, object)) return false;
        return hashCode() != object.hashCode();
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 51 + subtasks.hashCode();
    }


}
