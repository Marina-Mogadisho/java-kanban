package tasks;

import managers.ManagerSaveException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import java.time.ZoneId;

public class Task {

    private String title;
    private String description;
    private Integer id;
    private Status status;
    private Duration duration; // продолжительность задачи, оценка того, сколько времени она займёт в минутах.
    private LocalDateTime startTime; // дата и время, когда предполагается приступить к выполнению задачи.
    private Type type;
    private boolean lock;
    private static String format = "HH:mm dd.MM.yyyy";

    protected Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.lock = false;
        this.type = Type.TASK;
        this.duration = null;
        this.startTime = null;
    }

    public Task(String title, String description, Status status, String duration, String startTime) {
        this(title, description, status);

        if (duration == null || duration.length() == 0) this.duration = null;
        else
        if(duration.equals("0"))this.duration = null;
        else {
            try {
                this.duration = Duration.ofMinutes(Long.parseLong(duration));
            } catch (Exception e) {
                this.duration = null;
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        if (startTime == null || startTime.length() == 0) this.startTime = null;
        else
        if (startTime.equals("0"))this.startTime = null;
        else {
            try {
                //long seconds=Long.parseLong(startTime);
                //this.startTime=LocalDateTime.ofEpochSecond(seconds, 0, ZonedDateTime.now().getOffset());
                this.startTime = LocalDateTime.parse(startTime, formatter);

            } catch (Exception e) {
                this.startTime = null;
            }
        }
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

    /**
     * Метод проверки пересечения (intersection) задач по времени выполнения
     *
     * @return - если время окончания первого таска ПОСЛЕ времени начала второго таска, то true
     */
    public boolean intersection(Task t1) {
        if (getEndTime() == null || t1.getStartTime() == null) return false;
        if (getEndTime().isAfter(t1.getStartTime())) return true; // есть пересечение
        return false; // нет пересечения
    }

    public boolean isLock() {
        return lock;
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

        if (getStartTime() != null){
            time = formatter.format(getStartTime());
            //time = ""+getStartTime().getSecond();
        }
        else time = "0";

        String duration;
        if (getDuration() != null) duration = "" + getDuration().toMinutes();
        else duration = "0";
        return getId() + "," + getType() + "," + getTitle() + "," + getStatus() + "," + getDescription() + "," + time
                + "," + duration;
    }
}
