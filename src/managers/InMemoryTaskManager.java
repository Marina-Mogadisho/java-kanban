package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;
    private final HistoryManager historyManager;
    private int nextId;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.historyManager = historyManager;
        this.nextId = 1;
    }


    protected HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    protected HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    protected HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    /**
     * Получение нового уникального идентификатора
     */
    private Integer getId() {
        return nextId++;
    }

    public void setNextId(Integer n) {
        if (nextId < n) nextId = n;
    }

    /**
     * Вычисляем статус tasks.Epic
     */
    private void calcStatus(Epic epic) {
        if (epic == null) return;
        ArrayList<Integer> idSubtasks = epic.getAllSubtask(); // достали из tasks.Epic список id tasks.Subtask
        if (idSubtasks == null) return;
        if (idSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        boolean isNew = true;
        for (Integer idSubtask : idSubtasks) {
            Subtask subtask = subtasks.get(idSubtask);
            Status status = subtask.getStatus(); // достаем статус из подзадачи с помощью метода класса родителя tasks.Task
            if (status != Status.NEW) {
                isNew = false;
                break;
            }
        }
        if (isNew) {
            epic.setStatus(Status.NEW);
        } else {
            boolean isDone = true;
            for (Integer idSubtask : idSubtasks) {
                Subtask subtask = subtasks.get(idSubtask);
                Status status = subtask.getStatus();
                if (status != Status.DONE) {
                    isDone = false;
                    break;
                }
            }
            if (isDone) epic.setStatus(Status.DONE);
            else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        }
    }


    /**
     * Метод возвращает список 10 последних задач(или подзадачи или эпика), которые вызывались по id
     */
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    /**
     * Добавление задачи в список задач
     *
     * @return статус операции true/false
     */
    @Override
    public boolean addTask(Task newTask) throws ManagerSaveException {
        if (newTask == null) return false;
        Integer id = getId();  // Метод, увеличили счетчик newId на 1 и присвоили id
        newTask.setId(id);  // Метод из класса tasks.Task, добавили номер id в поле объекта задачи
        tasks.put(newTask.getId(), newTask); //положили задачу в общую коллекцию с задачами, HashMap tasks
        return true;
    }

    /**
     * Добавление подзадачи (tasks.Subtask), в нем есть поле idEpic
     *
     * @return статус операции true/false
     */
    @Override
    public boolean addSubtask(Subtask newSubtask) throws ManagerSaveException {
        if (newSubtask == null) return false;  // если newSubtask пустой - вышли с false
        if (newSubtask.getId() != null) return false; // если у newSubtask не пустое поле Id, вышли
        if (newSubtask.getIdEpic() == null) return false; // если у newSubtask пустое поле IdEpic, вышли
        Integer idEpic = newSubtask.getIdEpic(); //вытащили из newSubtask поле idEpic
        if (!epics.containsKey(idEpic)) return false; // если нет такого idEpic в коллекции tasks.Epic, вышли
        Integer id = getId(); // Метод, увеличили счетчик newId на 1 и присвоили id
        newSubtask.setId(id); // Метод из класса tasks.Task, добавили номер id в поле объекта задачи
        subtasks.put(newSubtask.getId(), newSubtask);//положили подзадачу в общую коллекцию HashMap subtasks
        Epic epic = epics.get(idEpic); //вытащила значение tasks.Epic, в котором есть поле ArrayList c id tasks.Subtask
        ArrayList<Integer> isSubtask = epic.getAllSubtask(); // вытащили из tasks.Epic ссылку на ее список  id tasks.Subtask
        isSubtask.add(newSubtask.getId()); // добавили в  ArrayList этого tasks.Epic новый id нового newSubtask
        calcStatus(epic);
        return true;
    }

    /**
     * Добавление tasks.Epic
     *
     * @return статус операции true/false
     */
    @Override
    public boolean addEpic(Epic epic) throws ManagerSaveException {
        if (epic == null) return false;
        if (epic.getId() != null) return false;
        Integer id = getId();  // Метод, увеличили счетчик newId на 1 и присвоили id
        epic.setId(id);  // Метод из класса tasks.Task, добавили номер id в поле объекта задачи
        epics.put(epic.getId(), epic); //положили задачу в общую коллекцию с задачами, HashMap tasks
        return true;
    }

    /**
     * Получение задачи (tasks.Task) по идентификатору (id)
     *
     * @param id идентификатор tasks.Task
     * @return ссылка на объект tasks.Task
     */
    @Override
    public Task getTaskById(Integer id) throws ManagerSaveException { //Получение по идентификатору.
        Task task = tasks.get(id);
        historyManager.addHistory(task);
        return task;
    }

    /**
     * Получение задачи Epic по идентификатору (id)
     *
     * @param id идентификатор Epic
     * @return ссылка на объект Epic
     */
    @Override
    public Epic getEpicById(Integer id) throws ManagerSaveException { //Получение по идентификатору.
        Epic epic = epics.get(id);
        if (epic == null) return null;
        historyManager.addHistory(epic);
        return epic;
    }

    /**
     * Получение tasks.Subtask по идентификатору (id)
     *
     * @param id идентификатор tasks.Subtask
     * @return ссылка на объект tasks.Subtask
     */
    @Override
    public Subtask getSubtaskById(Integer id) throws ManagerSaveException { //Получение по идентификатору.
        Subtask subtask = subtasks.get(id);
        if (subtask == null) return null;
        historyManager.addHistory(subtask);
        return subtask;
    }


    /**
     * Получение списка всех tasks.Task
     *
     * @return ссылка на объект ArrayList<tasks.Task>
     **/
    @Override
    public ArrayList<Task> getListAllTasks() throws ManagerSaveException {
        return new ArrayList<>(tasks.values());
    }

    /**
     * Получение списка всех tasks.Epic
     *
     * @return ссылка на объект ArrayList<tasks.Epic>
     **/
    @Override
    public ArrayList<Epic> getListAllEpic() throws ManagerSaveException {
        return new ArrayList<>(epics.values());
    }

    /**
     * Получение списка всех tasks.Subtask
     *
     * @return ссылка на объект ArrayList<tasks.Subtask>
     **/
    @Override
    public ArrayList<Subtask> getListAllSubtask() throws ManagerSaveException {
        return new ArrayList<>(subtasks.values());
    }

    /**
     * Получение списка tasks.Subtask по id tasks.Epic
     *
     * @return ссылка на объект Ha ArrayList<tasks.Subtask>
     **/
    @Override
    public ArrayList<Subtask> getListAllSubtaskForEpicId(Integer idEpic) throws ManagerSaveException {
        if (idEpic == null) return null;
        Epic epic = epics.get(idEpic);
        if (epic == null) return null;
        ArrayList<Integer> idListSubtask = epic.getAllSubtask();
        ArrayList<Subtask> outListSubtasks = new ArrayList<>();
        for (int i = 0; i < idListSubtask.size(); i++) {
            Integer isSubtask = idListSubtask.get(i);
            Subtask subtask = subtasks.get(isSubtask);
            outListSubtasks.add(subtask);
        }
        return outListSubtasks;
    }

    /**
     * Метод обновления tasks.Task
     *
     * @param newTask объект tasks.Task заменяющий старый
     * @return статус операции true/false
     */
    @Override
    public boolean updateTask(Task newTask) throws ManagerSaveException {
        if (newTask == null) return false;
        if (!tasks.containsKey(newTask.getId())) return false;
        if (tasks.containsValue(newTask)) return false;
        tasks.put(newTask.getId(), newTask); //положили задачу в общую коллекцию с задачами, HashMap tasks
        return true;
    }

    /**
     * Метод обновления tasks.Subtask и обновления статуса tasks.Epic
     *
     * @return статус операции true/false
     **/
    @Override
    public boolean updateSubtaskAndEpic(Subtask newSubtask) throws ManagerSaveException {
        if (newSubtask == null) return false;
        Integer idEpic = newSubtask.getIdEpic();
        if (idEpic == null) return false;
        Epic epic = epics.get(idEpic);
        if (epic == null) return false;
        if (subtasks.get(newSubtask.getId()) == null) return false;
        subtasks.put(newSubtask.getId(), newSubtask);
        calcStatus(epic);
        return true;
    }

    /**
     * Метод обновления tasks.Epic
     *
     * @return статус операции true/false*
     */
    @Override
    public boolean updateEpic(Epic newEpic) throws ManagerSaveException {
        if (newEpic == null) return false;
        if (!epics.containsKey(newEpic.getId())) return false;
        if (epics.containsValue(newEpic)) return false;
        epics.put(newEpic.getId(), newEpic); //положили задачу в общую коллекцию с задачами, HashMap tasks
        calcStatus(newEpic);
        return true;
    }


    /**
     * Удаление всех tasks.Task
     */
    @Override
    public boolean removeAllTasks() throws ManagerSaveException {
        if (!tasks.isEmpty()) {
            for (Task task : tasks.values()) {
                historyManager.removeFromHistory(task.getId());
                task.clearID();
            }
        }
        tasks.clear();
        return true;
    }

    /**
     * Удаление всех tasks.Epic и их tasks.Subtask
     **/
    @Override
    public boolean removeAllEpics() throws ManagerSaveException {
        if (!epics.isEmpty()) { // если мапа с epics не пустая
            for (Epic epic : epics.values()) {  // перебираем все эпики по очереди
                ArrayList<Integer> subtasks4epic = epic.getAllSubtask(); // достаем из эпика список id его subtask
                historyManager.removeFromHistory(epic.getId()); // удаляем текущий эпик из истории просмотров
                epic.clearID();  // удаляем id у текущего эпика
                for (Integer integer : subtasks4epic) { // перебираем все subtask по очереди
                    historyManager.removeFromHistory(integer); // удаляем текущий subtask из истории просмотров
                    Subtask subtask = subtasks.get(integer);
                    subtask.clearIDForSubtask(); // удаляем id у текущего subtask
                }
                subtasks4epic.clear(); // очищаем список subtask текущего эпика
            }
        }
        epics.clear();  // очищаем мапу с эпиками
        subtasks.clear(); // очищаем мапу с subtask
        return true;
    }

    /**
     * Удаление всех tasks.Subtask
     */
    @Override
    public boolean removeAllSubtasks() throws ManagerSaveException {
        if (!subtasks.isEmpty()) {   // если мапа с subtasks не пустая
            for (Subtask subtask : subtasks.values()) { // перебираем все subtask по очереди
                historyManager.removeFromHistory(subtask.getId());  // удаляем текущий subtask из истории просмотров
                subtask.clearIDForSubtask();
            }
            //subtasks.clear(); // очищаем мапу с subtask
            for (Epic epic : epics.values()) {
                ArrayList<Integer> isSubtask = epic.getAllSubtask();
                isSubtask.clear();
                calcStatus(epic);
            }
            subtasks.clear(); // очищаем мапу с subtask

        }
        return true;
    }


    /**
     * Удаление tasks.Task по идентификатору.
     *
     * @return статус операции true/false
     **/
    @Override
    public boolean removeByIdTask(Integer id) throws ManagerSaveException {
        Task task = tasks.remove(id);
        historyManager.removeFromHistory(id);// при удалении задачи она также удаляется из истории просмотров
        if (task != null) task.clearID();
        return task != null;
    }

    /**
     * Удаление tasks.Epic по идентификатору и удаление его tasks.Subtask
     *
     * @return статус операции true/false
     **/
    @Override
    public boolean removeByIdEpic(Integer id) throws ManagerSaveException {
        if (epics.get(id) == null) return false;
        Epic epic = getEpicById(id);
        if (epic == null) return false;
        ArrayList<Integer> subtasks4epic = epic.getAllSubtask();
        for (Integer sub : subtasks4epic) {
            Subtask removeSubtask = subtasks.remove(sub);
            historyManager.removeFromHistory(sub);
            if (removeSubtask != null) removeSubtask.clearIDForSubtask();

        }
        subtasks4epic.clear();
        epics.remove(id);
        historyManager.removeFromHistory(id);// при удалении задачи она также удаляется из истории просмотров
        epic.clearID();
        return true;
    }

    /**
     * Удаление tasks.Subtask по идентификатору и проверка изменения состояния tasks.Epic
     *
     * @return статус операции true/false
     **/
    @Override
    public boolean removeSubtaskById(Integer idSubtask) throws ManagerSaveException {
        if (idSubtask == null) return false;
        Subtask removeSubtask = subtasks.get(idSubtask);
        if (removeSubtask == null) return false;
        Integer idEpic = removeSubtask.getIdEpic();
        if (idEpic == null) return false;
        Epic epic = epics.get(idEpic);
        epic.getAllSubtask().remove(removeSubtask.getId());
        subtasks.remove(removeSubtask.getId());
        calcStatus(epic);

        historyManager.removeFromHistory(idSubtask);  // при удалении задачи она также удаляется из истории просмотров
        removeSubtask.clearID();
        return true;
    }
}



