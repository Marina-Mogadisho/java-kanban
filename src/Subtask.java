public class Subtask extends Task{

    private Integer id_epic;
    public Subtask(String title, String description, Status status) {
        super(title, description, status);  // установили параметры из конструктора родителя
    }
    public Integer getIdEpic() {
        return id_epic;
    }

    public void setIdEpic(Integer id_epic) {
        this.id_epic = id_epic;
    }


}
