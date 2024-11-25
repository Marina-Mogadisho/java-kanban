import java.util.Objects;

public class Task {

    private String title;
    private String description;
    private Integer id;
    private Status status;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return Objects.equals(title, task.title)
                && Objects.equals(description, task.description)
                && Objects.equals(id, task.id)
                && status == task.status;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (id != null) {
            hash = hash + id.hashCode();
        }
        hash = hash * 25;

        if (title != null) {
            hash = hash + title.hashCode();
        }
        hash = hash * 31;

        if (description != null) {
            hash = hash + description.hashCode();
        }
        hash = hash * 43;

        if (status != null) {
            hash = hash + status.hashCode();
        }

        return hash;
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
     */
    public void setId(Integer id) {  // Метод, берет на входе номер id и приравнивает его к полю id задачи
        this.id = id;
    }

    /**
     * Получение идентификатора задачи
     */
    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "id " + getId() + ", status " + getStatus() + ", title " + getTitle() +
                ", description " + getDescription();
    }
}
