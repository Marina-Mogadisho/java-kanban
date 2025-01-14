package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    boolean addTask(Task newTask);

    boolean addSubtask(Subtask newSubtask);

    boolean addEpic(Epic epic);

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    Subtask getSubtaskById(Integer id);

    ArrayList<Task> getListAllTasks();

    ArrayList<Epic> getListAllEpic();

    ArrayList<Subtask> getListAllSubtask();

    ArrayList<Subtask> getListAllSubtaskForEpicId(Integer idEpic);

    boolean updateTask(Task newTask);

    boolean updateSubtaskAndEpic(Subtask newSubtask);

    boolean updateEpic(Epic newEpic);

    void removeAllTasks();

    void removeAllEpics();

    boolean removeAllSubtasks();

    boolean removeByIdTask(Integer id);

    boolean removeByIdEpic(Integer id);

    boolean removeSubtaskById(Integer idSubtask);

    HistoryManager getHistoryManager();

}
