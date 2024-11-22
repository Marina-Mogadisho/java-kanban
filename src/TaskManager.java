import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

// Добавить в TaskManager метод добавления Subtask подзадач в Epic

    private HashMap<Integer, Task> tasks;
    private static int nextId = 1;

    public TaskManager() {
        tasks = new HashMap<>();
    }

    //// Добавить update по всем типам


    /**
     * Добавление задачи в список задач
     */
    public void addTask(Task task) {
        Integer id = getId();  // Метод, увеличили счетчик newId на 1 и присвоили id

        task.setId(id);  // Метод из класса Task, добавили номер id в поле объекта задачи

        tasks.put(task.getId(), task); //положили задачу в общую коллекцию с задачами, HashMap tasks
    }

    /**
     * Получение нового уникального идентификатора
     */
    public static Integer getId() {
        return nextId++;
    }

    /**
     * Получение задачи по идентификатору
     */
    public Task getById(Integer id) { //Получение по идентификатору.
        return tasks.get(id);
    }

    public boolean addSubtaskInEpic(Integer id_epic, Subtask new_subtask) {
        Task task = getById(id_epic);
        if (task == null) return false;
        task.addSubtask(new_subtask);
        return true;
    }


    /**
     * Получение списка всех Task
     **/
    public ArrayList<Task> getListAllTasks() {
        ArrayList<Task> listTask = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.getAllSubtask() == null) {
                // это Task
                listTask.add(task);
            } else {
                // это Epic
            }
        }
        return listTask;
    }


    /**
     * Получение списка Epic
     **/
    public ArrayList<Task> getListAllEpic() {
        ArrayList<Task> listTask = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.getAllSubtask() == null) {
                // это Task
            } else {
                // это Epic
                listTask.add(task);
            }
        }
        return listTask;
    }

    /**
     * Получение одного конкретного Subtask по id Epic и id Subtask
     **/
    public Subtask getSubtaskForEpicId(Integer idEpic, Integer idSubtask) {
        Task task = tasks.get(idEpic);
        HashMap<Integer, Subtask> subtasks = task.getAllSubtask();
        if (subtasks == null) {
            // это Task
            return null;
        } else {
            // это Epic0
            Subtask subtask = subtasks.get(idSubtask);
            return subtask;
        }
    }


    /**
     * Получение списка Subtask по id Epic
     **/
    public ArrayList<Subtask> getListAllSubtaskForEpicId(Integer idEpic) {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        Task task = tasks.get(idEpic);
        HashMap<Integer, Subtask> subtasks = task.getAllSubtask();
        if (subtasks == null) {
            // это Task
            return null;
        } else {
            // это Epic0
            for (Subtask subtask : subtasks.values()) {
                listSubtask.add(subtask);
            }
        }
        return listSubtask;
    }


    /**
     * Удаление всех Task
     */
    public void deleteAllTasks() {
        //Надо пробежаться по tasks и удалить те tasks, где метод getAllSubtask() возвращает null
        HashMap<Integer, Task> tasksForEpic = new HashMap<>();
        for (Task task : tasks.values()) {
            if (task.getAllSubtask() == null) {
                // это Task
            } else {
                // это Epic
                tasksForEpic.put(task.getId(), task);
            }
        }
        tasks = tasksForEpic;
    }

    /**
     * Удаление всех Epic
     **/
    public void deleteAllEpic() {
        //Надо пробежаться по tasks и удалить те tasks, где метод getAllSubtask() возвращает не !null
        HashMap<Integer, Task> tasksForTask = new HashMap<>();
        for (Task task : tasks.values()) {
            if (task.getAllSubtask() == null) {
                // это Task
                tasksForTask.put(task.getId(), task);
            } else {
                // это Epic
            }
        }
        tasks = tasksForTask;
    }

    /**
     * Удаление Task по идентификатору.
     **/
    public boolean removeByIdTask(Integer id) {
        Task task = tasks.get(id);
        if (task == null) return false;
        if (task.getAllSubtask() == null) {
            // это Task
            tasks.remove(id);
            return true;
        } else {
            // это Epic
            return false;
        }
    }


    /**
     * Удаление Epic по идентификатору
     **/
    public boolean removeByIdEpic(Integer id) {
        Task task = tasks.get(id);
        if (task == null) return false;
        if (task.getAllSubtask() == null) {
            // это Task
            return false;
        } else {
            // это Epic
            tasks.remove(id);
            return true;
        }
    }

    /**
     * Удаление Subtask по идентификатору Subtask
     **/
    public boolean removeByIdSubtask(Integer idSubtask) {
        for (Task task : tasks.values()) {
            if (task.getAllSubtask() == null) {
                // это Task
            } else {
                // это Epic
                removeByIdSubtask(task, idSubtask);
            }
        }
        return true;
    }

    /**
     * private Метод для удаления Subtask в конкретном Epic
     **/
    private boolean removeByIdSubtask(Task epic, Integer idSubtask) {
        HashMap<Integer, Subtask> allSubtasks = epic.getAllSubtask();
        if (allSubtasks == null) return false;
        allSubtasks.remove(idSubtask);
        return true;
    }
}



