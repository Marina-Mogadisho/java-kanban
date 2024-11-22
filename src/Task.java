import java.util.HashMap;

public class Task {
    private String title;
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

   /**
    * Установка идентификатора задачи
    * */
    public void setId(Integer id) {  // Метод, берет на входе номер newId и приравнивает его к полю id задачи
           this.id =  id;
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
