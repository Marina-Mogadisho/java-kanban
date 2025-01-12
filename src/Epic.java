import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> idSubtask;

    public Epic(String title, String description) {
        // конструктор родителя Task, который устанавливает параметры и статус
        super(title, description, Status.NEW);
        //subtasks = new HashMap<>(); // создаем коллекцию для подзадач, ссылку на коллекцию храним в subtasks
        idSubtask = new ArrayList<>();
    }

    public ArrayList<Integer> getAllSubtask() {
        return idSubtask;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!super.equals(object)) return false;
        if (!Objects.equals(this, object)) return false;
        int h1 = hashCode();
        int h2 = object.hashCode();
        return h1 == h2;
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 51 + idSubtask.hashCode();
    }
}
