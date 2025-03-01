package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> idSubtask;
    // дата и время, когда предполагается закончить выполнение задачи, по времени последнего Subtask.
    private LocalDateTime endTime;
    //private Duration duration;

    public Epic(String title, String description) {
        // конструктор родителя Task, который устанавливает параметры и статус
        super(title, description, Status.NEW);
        this.idSubtask = new ArrayList<>();
        this.setType(Type.EPIC);
        this.endTime = null;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(LocalDateTime time) {
        endTime = time;
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
        Epic newepic = (Epic) object;
        return idSubtask.equals(newepic.idSubtask);
    }


    @Override
    public int hashCode() {
        return super.hashCode() * 51 + idSubtask.hashCode();
    }
}
