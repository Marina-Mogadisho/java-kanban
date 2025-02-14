package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private final ArrayList<Integer> idSubtask;
    // дата и время, когда предполагается закончить выполнение задачи, по времени последнего Subtask.
    private LocalDateTime endTime;
    //private Duration duration;

    public Epic(String title, String description) {
        // конструктор родителя tasks.Task, который устанавливает параметры и статус
        super(title, description, Status.NEW);
        this.idSubtask = new ArrayList<>();
        this.setType(Type.EPIC);
        this.endTime=null;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(LocalDateTime time) {
           endTime=time;
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
