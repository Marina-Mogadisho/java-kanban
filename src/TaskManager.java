import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
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
    private static void calcStatus(Epic epic) {
        if (epic == null) return;
        HashMap<Integer, Subtask> subtasks = epic.getAllSubtask();
        if (subtasks == null) return;
        if (subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
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
        if (new_subtask == null) return false;
        Epic epic = getByIdEpic(id_epic);
        if (epic == null) return false;
        Integer new_id_subtask = getId(); // // увеличили счетчик ++ и присвоили id
        new_subtask.setId(new_id_subtask); // добавили id в поле объекта подзадачи
        new_subtask.setIdEpic(id_epic); // добавили id в поле объекта epic
        epic.getAllSubtask().put(new_subtask.getId(), new_subtask); // Добавляем новый Subtask c id в HashMap subtask Epic
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
        if (epic.getId() != null) return false;
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
    public Task getByIdTask(Integer id) { //Получение по идентификатору.
        return tasks.get(id);
    }

    /**
     * Получение задачи Epic по идентификатору (id)
     *
     * @return ссылка на объект Epic
     */
    public Epic getByIdEpic(Integer id) { //Получение по идентификатору.
        return epics.get(id);
    }

    /**
     * Получение списка всех Task
     *
     * @return ссылка на объект ArrayList<Task>
     **/
    public ArrayList<Task> getListAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    /**
     * Получение списка всех Epic
     *
     * @return ссылка на объект ArrayList<Task>
     **/
    public ArrayList<Epic> getListAllEpic() {
        return new ArrayList<>(epics.values());
    }

    /**
     * Получение одного конкретного Subtask по id Epic и id Subtask
     *
     * @return ссылка на объект HashMap<Integer, Subtask> subtasks
     **/
    public Subtask getSubtaskForEpicId(Integer idEpic, Integer idSubtask) {
        Epic epic = epics.get(idEpic);
        HashMap<Integer, Subtask> subtasks = epic.getAllSubtask();
        return subtasks.get(idSubtask);
    }

    /**
     * Получение списка Subtask по id Epic
     *
     * @return ссылка на объект Ha ArrayList<Subtask>
     **/
    public ArrayList<Subtask> getListAllSubtaskForEpicId(Integer id_epic) {
        Epic epic = epics.get(id_epic);
        if (epic == null) return null;
        HashMap<Integer, Subtask> subtasks = epic.getAllSubtask();
        return new ArrayList<>(subtasks.values());
    }

    /**
     * Метод обновления Task
     *
     * @return статус операции true/false
     */
    public boolean updateTask(Task new_task) {
        if (new_task == null) return false;
        if (!tasks.containsKey(new_task.getId())) return false;
        if (tasks.containsValue(new_task)) return false;
        tasks.put(new_task.getId(), new_task); //положили задачу в общую коллекцию с задачами, HashMap tasks
        return true;
    }

    /**
     * Метод обновления Subtask и обновления статуса Epic
     *
     * @return статус операции true/false
     **/
    public boolean updateSubtaskAndEpic(Subtask new_subtask) {
        if (new_subtask == null) return false;
        Integer id_new_subtask = new_subtask.getId(); // вытащили id new_subtask
        Integer id_epic = new_subtask.getIdEpic();
        Epic epic = epics.get(id_epic);
        if (epic == null) return false;
        HashMap<Integer, Subtask> subtasks = epic.getAllSubtask();
        //if (subtasks == null) return false;
        subtasks.put(new_subtask.getId(), new_subtask);
        calcStatus(epic);
        return true;
    }

    /**
     * Метод обновления Epic
     *
     * @return статус операции true/false*
     */
    public boolean updateEpic(Epic new_epic) {
        if (new_epic == null) return false;
        if (!epics.containsKey(new_epic.getId())) return false;
        if (epics.containsValue(new_epic)) return false;
        epics.put(new_epic.getId(), new_epic); //положили задачу в общую коллекцию с задачами, HashMap tasks
        return true;
    }


    /**
     * Удаление всех Task
     */
    public void removeAllTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    /**
     * Удаление всех Epic и их Subtask
     **/
    public void removeAllEpic() {
        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) {
                HashMap<Integer, Subtask> subtasks = epic.getAllSubtask();
                subtasks.clear();
            }
        }
        epics.clear();
    }


    /**
     * Удаление Task по идентификатору.
     *
     * @return статус операции true/false
     **/
    public boolean removeByIdTask(Integer id) {
        Task task = tasks.remove(id);
        return task == null;
    }

    /**
     * Удаление Epic по идентификатору и удаление его Subtask
     *
     * @return статус операции true/false
     **/
    public boolean removeByIdEpic(Integer id) {
        if (epics.get(id) == null) return false;
        Epic epic = getByIdEpic(id);
        HashMap<Integer, Subtask> subtasks = epic.getAllSubtask();
        subtasks.clear();
        epics.remove(id);
        return true;
    }

    /**
     * Удаление Subtask по идентификатору и проверка изменения состояния Epic
     *
     * @return статус операции true/false
     **/
    public boolean removeByIdSubtask(Integer id_subtask) {
        if (id_subtask == null) return false;
        // 1. По  id_subtask Вытащить id Epic для этого subtask
        // 2. Удалить  subtask по его id
        // 3. Проверить есть ли еще в этом Epic subtasks (не пуста ли HashMap subtasks этого Epic)
        // 4. Если HashMap subtasks этого Epic  пустая, то удалить Epic
        // 5. Если HashMap subtasks этого Epic  НЕ пустая, то высчитать новый статус для этого Epic
        for (Epic epic : epics.values()) {
            if (removeByIdSubtask(epic, id_subtask)) {
                calcStatus(epic);
                break;
            }
        }
        return true;
    }

    /**
     * private Метод для удаления Subtask в конкретном Epic
     **/
    private boolean removeByIdSubtask(Epic epic, Integer idSubtask) {
        HashMap<Integer, Subtask> allSubtasks = epic.getAllSubtask();
        Subtask t = allSubtasks.remove(idSubtask);
        return t != null;
    }
}



