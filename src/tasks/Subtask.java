package tasks;

import java.util.Objects;

public class Subtask extends Task {

    private Integer idEpic;
    private boolean lockEpic;

    public Subtask(String title, String description, Status status, String duration, String startTime) {
        super(title, description, status, duration, startTime);  // установили параметры из конструктора родителя
        lockEpic = false;
        this.setType(Type.SUBTASK);
    }

    public Subtask(Integer idEpic, String title, String description, Status status, String duration, String startTime) {
        super(title, description, status, duration, startTime);  // установили параметры из конструктора родителя
        lockEpic = false;
        setIdEpic(idEpic);
        this.setType(Type.SUBTASK);

    }

    public Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer idEpic) {
        if (lockEpic) return;
        this.idEpic = idEpic;
        lockEpic = true;
    }

    public void clearIDForSubtask() {
        super.clearID();
        lockEpic = false;
        idEpic = 0;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!super.equals(object)) return false;
        if (!Objects.equals(this, object)) return false;
        return hashCode() == object.hashCode();
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 57 + idEpic.hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + "," + getIdEpic();
    }
}
