import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private int nextId;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        nextId = 1;
    }

    /**
     * Получение нового уникального идентификатора
     */
    private Integer getId() {
        return nextId++;
    }

    /**
     * Вычисляем статус Epic
     */
    private static void calcStatus(Task epic) {
        if (epic == null) return;
        HashMap<Integer, Subtask> subtasks = epic.getAllSubtask();
        if (subtasks == null) return;
        boolean is_new = true;
        for (Subtask subtask : subtasks.values()) {
            Status status = subtask.getStatus(); // достаем статус из подзадачи с помощью метода класса родителя Task
            if (status != Status.NEW) {
                is_new = false;
                break;
            }
        }
        if (is_new) {
            epic.setStatus(Status.NEW);
        } else {
            boolean is_done = true;
            for (Subtask subtask : subtasks.values()) {
                Status status = subtask.getStatus();
                if (status != Status.DONE) {
                    is_done = false;
                    break;
                }
            }
            if (is_done) epic.setStatus(Status.DONE);
            else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }

    /**
     * Добавление задачи в список задач
     *
     * @return статус операции true/false
     */
    public boolean addTask(Task task) {
        if (task == null) return false;
        Integer id = getId();  // Метод, увеличили счетчик newId на 1 и присвоили id
        task.setId(id);  // Метод из класса Task, добавили номер id в поле объекта задачи
        tasks.put(task.getId(), task); //положили задачу в общую коллекцию с задачами, HashMap tasks
        return true;
    }

    /**
     * Добавление подзадачи (Subtask) в Epic по идентификатору (id) Epic
     *
     * @return статус операции true/false
     */
    public boolean addSubtaskInEpic(Integer id_epic, Subtask new_subtask) {
        Task epic = getById(id_epic);
        if (epic == null) return false;
        Integer new_id_subtask = getId(); // // увеличили счетчик ++ и присвоили id
        new_subtask.setId(new_id_subtask); // добавили id в поле объекта задачи
        new_subtask.setIdEpic(id_epic); // добавили id в поле объекта задачи
        epic.getAllSubtask().put(new_subtask.getId(), new_subtask);
        calcStatus(epic);
        return true;
    }

    /**
     * Добавление Epic
     *
     * @return статус операции true/false
     */
    public boolean addEpic(Epic epic) {
        if (epic == null) return false;
        Integer id = getId();  // Метод, увеличили счетчик newId на 1 и присвоили id
        epic.setId(id);  // Метод из класса Task, добавили номер id в поле объекта задачи
        epics.put(epic.getId(), epic); //положили задачу в общую коллекцию с задачами, HashMap tasks
        return true;
    }

    /**
     * Получение задачи (Task) по идентификатору (id)
     *
     * @return ссылка на объект Task
     */
    public Task getById(Integer id) { //Получение по идентификатору.
        return tasks.get(id);
    }

    /**
     * Получение списка всех Task
     *
     * @return ссылка на объект ArrayList<Task>
     **/
    public ArrayList<Task> getListAllTasks() {
        ArrayList<Task> listTask = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.getAllSubtask() == null) {
                // это Task
                listTask.add(task);
            }
            //else {
            // это Epic
            //}
        }
        return listTask;
    }

    /**
     * Получение списка всех Epic
     *
     * @return ссылка на объект ArrayList<Task>
     **/
    public ArrayList<Task> getListAllEpic() {
        ArrayList<Task> listTask = new ArrayList<>();
        for (Task task : epics.values()) {
            if (task.getAllSubtask() != null) {
                // это Epic
                listTask.add(task);
            }
            //else {
            // это Task
            //}
        }
        return listTask;
    }

    /**
     * Получение одного конкретного Subtask по id Epic и id Subtask
     *
     * @return ссылка на объект HashMap<Integer, Subtask> subtasks
     **/
    public Subtask getSubtaskForEpicId(Integer idEpic, Integer idSubtask) {
        Task task = tasks.get(idEpic);
        HashMap<Integer, Subtask> subtasks = task.getAllSubtask();
        if (subtasks == null) {
            // это Task
            return null;
        } else {
            // это Epic0
            return subtasks.get(idSubtask);
        }
    }

    /**
     * Получение списка Subtask по id Epic
     *
     * @return ссылка на объект Ha ArrayList<Subtask>
     **/
    public ArrayList<Subtask> getListAllSubtaskForEpicId(Integer idEpic) {
        ArrayList<Subtask> listSubtask = new ArrayList<>();
        Task task = tasks.get(idEpic);
        if (task == null) return null;
        HashMap<Integer, Subtask> subtasks = task.getAllSubtask();
        if (subtasks == null) {
            // это Task
            return null;
        } else {
            // это Epic
            //listSubtask = new ArrayList<>(subtasks.values());
            for (Subtask subtask : subtasks.values()) {
                listSubtask.add(subtask);
            }
        }
        return listSubtask;
    }

    /**
     * Метод обновления Task
     *
     * @return статус операции true/false
     */
    public boolean updateTask(Task new_task) {
        if (new_task == null) return false;
        if (!tasks.containsKey(new_task.getId())) return false;
        if (!tasks.containsValue(new_task)) return false;
        addTask(new_task);
        return true;
    }

    /**
     * Метод обновления Subtask и обновления статуса Epic
     *
     * @return статус операции true/false
     **/
    public boolean updateSubtask(Subtask new_subtask) {
        if (new_subtask == null) return false;
        Integer id_epic = new_subtask.getIdEpic();
        Task epic = tasks.get(id_epic);
        if (epic == null) return false;
        HashMap<Integer, Subtask> subtasks = epic.getAllSubtask();
        if (subtasks == null) return false;
        subtasks.put(new_subtask.getId(), new_subtask);
        calcStatus(epic);
        return true;
    }

    /**
     * Метод обновления Epic
     *
     * @return статус операции true/false*
     */
    public boolean updateEpic(Epic epic) {
        if (epic == null) return false;
        addTask(epic);
        return true;
    }


    /**
     * Удаление всех Task
     */
    public void removeAllTasks() {
        //Надо пробежаться по tasks и удалить те tasks, где метод getAllSubtask() возвращает null
        HashMap<Integer, Task> tasksForEpic = new HashMap<>();
        for (Task task : tasks.values()) {
            if (task.getAllSubtask() != null) {
                // это Epic
                tasksForEpic.put(task.getId(), task);
                // это Task
            }
            //else {
            // это Task
            //}
        }
        tasks = tasksForEpic;
    }

    /**
     * Удаление всех Epic
     **/
    public void removeAllEpic() {
        //Надо пробежаться по tasks и удалить те tasks, где метод getAllSubtask() возвращает не !null
        HashMap<Integer, Task> tasksForTask = new HashMap<>();
        for (Task task : tasks.values()) {
            if (task.getAllSubtask() == null) {
                // это Task
                tasksForTask.put(task.getId(), task);
            }
            //else {
            // это Epic
            //}
        }
        tasks = tasksForTask;
    }


    /**
     * Удаление Task по идентификатору.
     *
     * @return статус операции true/false
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
     *
     * @return статус операции true/false
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
     *
     * @return статус операции true/false
     **/
    public boolean removeByIdSubtask(Integer idSubtask) {
        for (Task task : tasks.values()) {
            if (task.getAllSubtask() != null) {
                // это Epic
                if (removeByIdSubtask(task, idSubtask)) break;
                // это Task
            }
            //else {
            // это Task
            //}
        }
        return true;
    }

    /**
     * private Метод для удаления Subtask в конкретном Epic
     **/
    private boolean removeByIdSubtask(Task epic, Integer idSubtask) {
        HashMap<Integer, Subtask> allSubtasks = epic.getAllSubtask();
        if (allSubtasks == null) return false;
        Task t = allSubtasks.remove(idSubtask);
        return t != null;
    }
}



