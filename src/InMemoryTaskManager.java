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

    public HistoryManager getHistoryManager() {
        return historyManager;
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
    private void calcStatus(Epic epic) {
        if (epic == null) return;
        ArrayList<Integer> idSubtasks = epic.getAllSubtask(); // достали из Epic список id Subtask
        if (idSubtasks == null) return;
        if (idSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        boolean isNew = true;
        for (Integer idSubtask : idSubtasks) {
            Subtask subtask = subtasks.get(idSubtask);
            Status status = subtask.getStatus(); // достаем статус из подзадачи с помощью метода класса родителя Task
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
    public boolean addTask(Task newTask) {
        if (newTask == null) return false;
        Integer id = getId();  // Метод, увеличили счетчик newId на 1 и присвоили id
        newTask.setId(id);  // Метод из класса Task, добавили номер id в поле объекта задачи
        tasks.put(newTask.getId(), newTask); //положили задачу в общую коллекцию с задачами, HashMap tasks
        return true;
    }

    /**
     * Добавление подзадачи (Subtask), в нем есть поле idEpic
     *
     * @return статус операции true/false
     */
    @Override
    public boolean addSubtask(Subtask newSubtask) {
        if (newSubtask == null) return false;  // если newSubtask пустой - вышли с false
        if (newSubtask.getId() != null) return false; // если у newSubtask не пустое поле Id, вышли
        if (newSubtask.getIdEpic() == null) return false; // если у newSubtask пустое поле IdEpic, вышли
        Integer idEpic = newSubtask.getIdEpic(); //вытащили из newSubtask поле idEpic
        if (!epics.containsKey(idEpic)) return false; // если нет такого idEpic в коллекции Epic, вышли
        Integer id = getId(); // Метод, увеличили счетчик newId на 1 и присвоили id
        newSubtask.setId(id); // Метод из класса Task, добавили номер id в поле объекта задачи
        subtasks.put(newSubtask.getId(), newSubtask);//положили подзадачу в общую коллекцию HashMap subtasks
        Epic epic = epics.get(idEpic); //вытащила значение Epic, в котором есть поле ArrayList c id Subtask
        ArrayList<Integer> isSubtask = epic.getAllSubtask(); // вытащили из Epic ссылку на ее список  id Subtask
        isSubtask.add(newSubtask.getId()); // добавили в  ArrayList этого Epic новый id нового newSubtask
        calcStatus(epic);
        return true;
    }


    /**
     * Добавление Epic
     *
     * @return статус операции true/false
     */
    @Override
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
     * @param id идентификатор Task
     * @return ссылка на объект Task
     */
    @Override
    public Task getTaskById(Integer id) { //Получение по идентификатору.
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
    public Epic getEpicById(Integer id) { //Получение по идентификатору.
        Epic epic = epics.get(id);
        historyManager.addHistory(epic);
        return epic;
    }

    /**
     * Получение Subtask по идентификатору (id)
     *
     * @param id идентификатор Subtask
     * @return ссылка на объект Subtask
     */
    @Override
    public Subtask getSubtaskById(Integer id) { //Получение по идентификатору.
        Subtask subtask = subtasks.get(id);
        historyManager.addHistory(subtask);
        return subtask;
    }


    /**
     * Получение списка всех Task
     *
     * @return ссылка на объект ArrayList<Task>
     **/
    @Override
    public ArrayList<Task> getListAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    /**
     * Получение списка всех Epic
     *
     * @return ссылка на объект ArrayList<Epic>
     **/
    @Override
    public ArrayList<Epic> getListAllEpic() {
        return new ArrayList<>(epics.values());
    }

    /**
     * Получение списка всех Subtask
     *
     * @return ссылка на объект ArrayList<Subtask>
     **/
    @Override
    public ArrayList<Subtask> getListAllSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    /**
     * Получение списка Subtask по id Epic
     *
     * @return ссылка на объект Ha ArrayList<Subtask>
     **/
    @Override
    public ArrayList<Subtask> getListAllSubtaskForEpicId(Integer idEpic) {
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
     * Метод обновления Task
     *
     * @param newTask объект Task заменяющий старый
     * @return статус операции true/false
     */
    @Override
    public boolean updateTask(Task newTask) {
        if (newTask == null) return false;
        if (!tasks.containsKey(newTask.getId())) return false;
        if (tasks.containsValue(newTask)) return false;
        tasks.put(newTask.getId(), newTask); //положили задачу в общую коллекцию с задачами, HashMap tasks
        return true;
    }

    /**
     * Метод обновления Subtask и обновления статуса Epic
     *
     * @return статус операции true/false
     **/
    @Override
    public boolean updateSubtaskAndEpic(Subtask newSubtask) {
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
     * Метод обновления Epic
     *
     * @return статус операции true/false*
     */
    @Override
    public boolean updateEpic(Epic newEpic) {
        if (newEpic == null) return false;
        if (!epics.containsKey(newEpic.getId())) return false;
        if (epics.containsValue(newEpic)) return false;
        epics.put(newEpic.getId(), newEpic); //положили задачу в общую коллекцию с задачами, HashMap tasks
        calcStatus(newEpic);
        return true;
    }


    /**
     * Удаление всех Task
     */
    @Override
    public void removeAllTasks() {
        if (!tasks.isEmpty()) {
            for (Task task : tasks.values()) {
                historyManager.removeFromHistory(task.getId());
                task.clearID();
            }
        }
        tasks.clear();
    }

    /**
     * Удаление всех Epic и их Subtask
     **/
    @Override
    public void removeAllEpics() {
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
    }

    /**
     * Удаление всех Subtask
     */
    @Override
    public boolean removeAllSubtasks() {
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
     * Удаление Task по идентификатору.
     *
     * @return статус операции true/false
     **/
    @Override
    public boolean removeByIdTask(Integer id) {
        Task task = tasks.remove(id);
        historyManager.removeFromHistory(id);// при удалении задачи она также удаляется из истории просмотров
        if (task != null) task.clearID();
        return task != null;
    }

    /**
     * Удаление Epic по идентификатору и удаление его Subtask
     *
     * @return статус операции true/false
     **/
    @Override
    public boolean removeByIdEpic(Integer id) {
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
     * Удаление Subtask по идентификатору и проверка изменения состояния Epic
     *
     * @return статус операции true/false
     **/
    @Override
    public boolean removeSubtaskById(Integer idSubtask) {
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



