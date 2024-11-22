import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private static int nextId = 1;

    public TaskManager() {
        tasks = new HashMap<>();
    }

    //// Добавить update по всем типам

    /**
     * Получение нового уникального идентификатора
     */
    public static Integer getId() {
        return nextId++;
    }

    /**
     * Добавление задачи в список задач
     */
    public boolean addTask(Task task) {
        Integer id = getId();  // Метод, увеличили счетчик newId на 1 и присвоили id
        if (id != null) {
            task.setId(id);  // Метод из класса Task, добавили номер id в поле объекта задачи
            tasks.put(task.getId(), task); //положили задачу в общую коллекцию с задачами, HashMap tasks
        }
        return false;
    }



    /**
     * Метод обновления Subtask и обновления статуса Epic
     **/
    public boolean updateSubtask(Subtask new_subtask) {
        if (new_subtask == null) return false;
        Integer id_epic = new_subtask.getIdEpic();
        Task epic = tasks.get(id_epic);
        if (epic == null) return false;
        HashMap<Integer, Subtask> subtasks = epic.getAllSubtask();
        if(subtasks == null) return false;
        subtasks.put(new_subtask.getId(), new_subtask);
        calcStatus(epic);
        return true;
    }

    /**
     * Добавление подзадачи (Subtask) в Epic по идентификатору (id) Epic
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

    private static void calcStatus(Task epic) {
        if (epic == null) return;
        HashMap<Integer, Subtask> subtasks = epic.getAllSubtask();
        if (subtasks == null) return;
        boolean is_new = true;
        for (Subtask subtask : subtasks.values()) {
            Status status = subtask.getStatus(); // достаем статус из подзадачи с помощью метода класса родителя Task
            // проверяем что все элементы
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
                // проверяем что все элементы
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


    public void addEpic(Epic epic) {
        addTask(epic);
    }

    public void updateTask(Task task) {
        addTask(task);
    }

    public void updateEpic(Epic epic) {
        addTask(epic);
    }

    /**
     * Получение задачи по идентификатору
     */
    public Task getById(Integer id) { //Получение по идентификатору.
        return tasks.get(id);
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
     * Получение списка всех Epic
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
        if (task == null) return null;
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
    public void removeAllTasks() {
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
    public void removeAllEpic() {
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
                if (removeByIdSubtask(task, idSubtask)) break;
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
        Task t = allSubtasks.remove(idSubtask);
        if (t != null) return true;
        return false;
    }
}



