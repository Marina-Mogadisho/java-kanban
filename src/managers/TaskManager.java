package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    boolean addTask(Task newTask) throws ManagerSaveException;

    boolean addSubtask(Subtask newSubtask) throws ManagerSaveException;

    boolean addEpic(Epic epic) throws ManagerSaveException;

    Task getTaskById(Integer id) throws ManagerSaveException;

    Epic getEpicById(Integer id) throws ManagerSaveException;

    Subtask getSubtaskById(Integer id) throws ManagerSaveException;

    ArrayList<Task> getListAllTasks() throws ManagerSaveException;

    ArrayList<Epic> getListAllEpic() throws ManagerSaveException;

    ArrayList<Subtask> getListAllSubtask() throws ManagerSaveException;

    List<Subtask> getListAllSubtaskForEpicId(Integer idEpic) throws ManagerSaveException;

    boolean updateTask(Task newTask) throws ManagerSaveException;

    boolean updateSubtaskAndEpic(Subtask newSubtask) throws ManagerSaveException;

    boolean updateEpic(Epic newEpic) throws ManagerSaveException;

    boolean removeAllTasks() throws ManagerSaveException;

    boolean removeAllEpics() throws ManagerSaveException;

    boolean removeAllSubtasks() throws ManagerSaveException;

    boolean removeByIdTask(Integer id) throws ManagerSaveException;

    boolean removeByIdEpic(Integer id) throws ManagerSaveException;

    boolean removeSubtaskById(Integer idSubtask) throws ManagerSaveException;

    HistoryManager getHistoryManager();

    ArrayList<Task> getPrioritizedTasks();
}
