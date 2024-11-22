import java.util.HashMap;

public class Task {
    private String title;

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String description;
    private Integer id;
    private Status status;
    /**
     * Конструктор
     * */
    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }
   /**
    * Установка идентификатора задачи
    * */
    public void setId(Integer newId) {  // Метод, берет на входе номер newId и приравнивает его к полю id задачи
        if(id == null) {
            id = newId;
        }
    }
    /**
     * Получение идентификатора задачи
     * */
    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }
    public void addSubtask(Subtask subtask) {
    }

    public HashMap<Integer, Subtask> getAllSubtask() {
        return null;
    }



    @Override
    public String toString() {

        return "id " + id + ", status " + status + ", title " + title + ", description " + description;
    }
}
