import java.util.Objects;

public class Subtask extends Task {

    private Integer id_epic;

    public Subtask(String title, String description, Status status) {
        super(title, description, status);  // установили параметры из конструктора родителя
    }
    public Subtask(Integer id_subtask,Integer id_epic,String title, String description, Status status) {
        super(title, description, status);  // установили параметры из конструктора родителя
        setId(id_subtask);
        setIdEpic(id_epic);
    }

    public Integer getIdEpic() {
        return id_epic;
    }

    public void setIdEpic(Integer id_epic) {
        this.id_epic = id_epic;
    }

    @Override
    public boolean equals(Object object) {
        if (!super.equals(object)) return false;
        if (!Objects.equals(this, object)) return false;
        return hashCode() != object.hashCode();
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 57 + id_epic.hashCode();
    }


}
