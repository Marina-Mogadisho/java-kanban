package tasks;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import util.*;

public class Task {

    private String title;
    private String description;
    private Integer id;
    private Status status;
    private Duration duration; // продолжительность задачи, оценка того, сколько времени она займёт в минутах.
    private LocalDateTime startTime; // дата и время, когда предполагается приступить к выполнению задачи.
    private Type type;
    private boolean lock;
    private static String format = UtilTime.format;

    protected Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.lock = false;
        this.type = Type.TASK;
        this.duration = null;
        this.startTime = null;
    }

    public Task(String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        this(title, description, status);

        this.duration = duration;
        this.startTime = startTime;
    }

    /**
     * Установка идентификатора задачи
     */
    public void setId(Integer id) {  // Метод, берет на входе номер id и приравнивает его к полю id задачи
        if (lock) return;
        this.id = id;
        lock = true;
    }

    public void clearID() {
        this.lock = false;
        this.id = 0;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Метод находит время окончания выполнения задачи типа Task (подходит для Subtask)
     *
     * @return время и дату типа LocalDateTime
     */
    public LocalDateTime getEndTime() {
        if (startTime == null) return null;
        return startTime.plus(duration);
    }

    public void setEndTime(LocalDateTime time) {
    }

    public void setLock(boolean lock) {
        this.lock = lock;
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
     * Получение идентификатора задачи
     */
    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
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

    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String time;

        if (getStartTime() != null) {
            time = formatter.format(getStartTime());
            //time = ""+getStartTime().getSecond();
        } else time = "0";

        String duration;
        if (getDuration() != null) duration = "" + getDuration().toMinutes();
        else duration = "0";
        return getId() + "," + getType() + "," + getTitle() + "," + getStatus() + "," + getDescription() + "," + time
                + "," + duration;
    }
}
