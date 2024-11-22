import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtasks;

    public Epic(String title, String description) {
        // конструктор родителя Task, который устанавливает параметры и статус
        super(title, description, Status.NEW);
        subtasks = new HashMap<>(); // создаем коллекцию для подзадач, ссылку на коллекцию храним в subtasks
    }


    @Override
    public HashMap<Integer, Subtask> getAllSubtask() {
        return subtasks;
    }
    @Override
    public String toString() {
           return super.toString()+" subtask:"+subtasks.values()+" ";
    }

}
