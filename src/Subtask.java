import java.util.Objects;

public class Subtask extends Task {

    private Integer idEpic;

    public Subtask(String title, String description, Status status) {
        super(title, description, status);  // установили параметры из конструктора родителя
    }

    public Subtask(Integer id_epic, String title, String description, Status status) {
        super(title, description, status);  // установили параметры из конструктора родителя
        setIdEpic(id_epic);
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer id_epic) {
        this.idEpic = id_epic;
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


}
