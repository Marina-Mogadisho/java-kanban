package managers;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;
    private final TreeSet<Task> tasksTreeSet;
    private final HistoryManager historyManager;
    private int nextId;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.tasksTreeSet = new TreeSet<>(new CompareTask());
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
     * Метод высчитывает время старта и продолжительность Эпика
     * продолжительность Эпика это сумма продолжительностей всех его активных подзадач
     */
    private void calcTimeForEpic(Epic epic) throws ManagerSaveException {
        if (epic == null) return;
        List<Subtask> subtasksForEpic = getListAllSubtaskForEpicId(epic.getId()); // вытащили список subtask этого эпика
        if (subtasksForEpic.isEmpty()) return;

        LocalDateTime minStartTime = subtasksForEpic.getFirst().getStartTime(); // стартовое время выполнения 1ой subtask
        if (minStartTime == null) return;
        LocalDateTime maxEndTime = subtasksForEpic.getFirst().getEndTime(); // время окончания выполнения 1ой subtask
        if (maxEndTime == null) return;

        Duration durationSubtasks = Duration.ofMinutes(0);
        for (Subtask subtask : subtasksForEpic) {  // вытащили из коллекции subtask
            if (subtask == null) continue;
            LocalDateTime startTimeSubtask = subtask.getStartTime(); // нашли стартовое время subtask
            LocalDateTime endTimeSubtask = subtask.getEndTime(); // нашли время окончания subtask
            Duration duration = subtask.getDuration();
            if (duration == null) return;
            durationSubtasks = durationSubtasks.plus(duration);

            if (startTimeSubtask == null) continue;
            if (endTimeSubtask == null) continue;

            if (minStartTime.isAfter(startTimeSubtask)) {
                minStartTime = startTimeSubtask;
            }

            if (maxEndTime.isBefore(endTimeSubtask)) {
                maxEndTime = endTimeSubtask;
            }
        }
        epic.setStartTime(minStartTime);
        epic.setEndTime(maxEndTime);
        epic.setDuration(durationSubtasks);
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
     * Метод возвращает отсортированный по стартовому времени список Task и Subtask
     */
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(tasksTreeSet);
    }

    /**
     * Метод проверки пересечения (intersection) задач по времени выполнения
     *
     * @return - если время окончания первого таска ПОСЛЕ времени начала второго таска, то true
     */
    public boolean intersectionTask(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getEndTime() == null) return false;
        for (Task task : tasksTreeSet) {
            if (task.getEndTime() == null || task.getStartTime() == null) return false;
            if (task.getEndTime().isAfter(newTask.getStartTime())) return true;// есть пересечение
        }
        return false;
    }

    /**
     * Добавление задачи в список задач
     *
     * @return статус операции true/false
     */
    @Override
    public boolean addTask(Task newTask) throws ManagerSaveException {
        if (newTask == null) return false;
        if (intersectionTask(newTask)) return false; // если newTask пересекается по времени с другой подзадачей
        Integer id = getId();  // Метод, увеличили счетчик newId на 1 и присвоили id
        newTask.setId(id);  // Метод из класса Task, добавили номер id в поле объекта задачи
        tasks.put(newTask.getId(), newTask); //положили задачу в общую коллекцию с задачами, HashMap tasks
        if (newTask.getStartTime() != null) tasksTreeSet.add(newTask);
        return true;
    }

    /**
     * Добавление подзадачи Subtask, в нем есть поле idEpic
     *
     * @return статус операции true/false
     */
    @Override
    public boolean addSubtask(Subtask newSubtask) throws ManagerSaveException {
        if (newSubtask == null) return false;  // если newSubtask пустой - вышли с false
        if (newSubtask.getId() != null) return false; // если у newSubtask не пустое поле Id, вышли
        if (newSubtask.getIdEpic() == null) return false; // если у newSubtask пустое поле IdEpic, вышли
        Integer idEpic = newSubtask.getIdEpic(); //вытащили из newSubtask поле idEpic
        if (!epics.containsKey(idEpic)) return false; // если нет такого idEpic в коллекции Epic, вышли
        if (intersectionTask(newSubtask))
            return false;  // если newSubtask пересекается по времени с другой подзадачей
        Integer id = getId(); // Метод, увеличили счетчик newId на 1 и присвоили id
        newSubtask.setId(id); // Метод из класса Task, добавили номер id в поле объекта задачи
        subtasks.put(newSubtask.getId(), newSubtask);//положили подзадачу в общую коллекцию HashMap subtasks
        Epic epic = epics.get(idEpic); //вытащила значение Epic, в котором есть поле ArrayList c id Subtask
        ArrayList<Integer> isSubtask = epic.getAllSubtask(); // вытащили из Epic ссылку на ее список id Subtask
        isSubtask.add(newSubtask.getId()); // добавили в  ArrayList этого tasks.Epic новый id нового newSubtask
        calcStatus(epic);
        calcTimeForEpic(epic);
        if (newSubtask.getStartTime() != null) tasksTreeSet.add(newSubtask);
        return true;
    }

    /**
     * Добавление Epic
     *
     * @return статус операции true/false
     */
    @Override
    public boolean addEpic(Epic newEpic) throws ManagerSaveException {
        if (newEpic == null) return false;
        if (newEpic.getId() != null) return false;
        Integer id = getId();  // Метод, увеличили счетчик newId на 1 и присвоили id
        newEpic.setId(id);  // Метод из класса Task, добавили номер id в поле объекта задачи
        epics.put(newEpic.getId(), newEpic); //положили задачу в общую коллекцию с задачами, HashMap tasks
        return true;
    }

    /**
     * Получение задачи Task по идентификатору (id)
     *
     * @param id идентификатор Task
     * @return ссылка на объект Task
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
     * Получение Subtask по идентификатору (id)
     *
     * @param id идентификатор Subtask
     * @return ссылка на объект Subtask
     */
    @Override
    public Subtask getSubtaskById(Integer id) throws ManagerSaveException { //Получение по идентификатору.
        Subtask subtask = subtasks.get(id);
        if (subtask == null) return null;
        historyManager.addHistory(subtask);
        return subtask;
    }

    /**
     * Получение списка всех Task
     *
     * @return ссылка на объект ArrayList<tasks.Task>
     **/
    @Override
    public ArrayList<Task> getListAllTasks() throws ManagerSaveException {
        return new ArrayList<>(tasks.values());
    }

    /**
     * Получение списка всех Epic
     *
     * @return ссылка на объект ArrayList<tasks.Epic>
     **/
    @Override
    public ArrayList<Epic> getListAllEpic() throws ManagerSaveException {
        return new ArrayList<>(epics.values());
    }

    /**
     * Получение списка всех Subtask
     *
     * @return ссылка на объект ArrayList<tasks.Subtask>
     **/
    @Override
    public ArrayList<Subtask> getListAllSubtask() throws ManagerSaveException {
        return new ArrayList<>(subtasks.values());
    }

    /**
     * Получение списка Subtask по id tasks.Epic
     *
     * @return ссылка на объект Ha ArrayList<tasks.Subtask>
     **/
    @Override
    public List<Subtask> getListAllSubtaskForEpicId(Integer idEpic) throws ManagerSaveException {
        if (idEpic == null) return null;
        Epic epic = epics.get(idEpic);
        if (epic == null) return null;
        ArrayList<Integer> idListSubtask = epic.getAllSubtask();
        return idListSubtask.stream()
                .map(idSubtask -> subtasks.get(idSubtask))
                .collect(Collectors.toList());
    }

    /**
     * Метод обновления Task
     *
     * @param newTask объект Task заменяющий старый
     * @return статус операции true/false
     */
    @Override
    public boolean updateTask(Task newTask) throws ManagerSaveException {
        if (newTask == null) return false;
        if (!tasks.containsKey(newTask.getId())) return false;
        if (tasks.containsValue(newTask)) return false;
        Task task = tasks.put(newTask.getId(), newTask); //положили задачу в общую коллекцию с задачами, HashMap tasks
        if (task != null) tasksTreeSet.remove(task);
        tasksTreeSet.add(newTask);
        return true;
    }

    /**
     * Метод обновления Subtask и обновления статуса Epic
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
        Subtask subtask = subtasks.put(newSubtask.getId(), newSubtask);
        calcStatus(epic);
        calcTimeForEpic(epic);
        if (subtask != null) tasksTreeSet.remove(subtask);
        tasksTreeSet.add(newSubtask);
        return true;
    }

    /**
     * Метод обновления Epic
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
        calcTimeForEpic(newEpic);
        return true;
    }

    /**
     * Удаление всех Task
     */
    @Override
    public boolean removeAllTasks() throws ManagerSaveException {
        if (!tasks.isEmpty()) {
            for (Task task : tasks.values()) {
                historyManager.removeFromHistory(task.getId());
                tasksTreeSet.remove(task);
                task.clearID();
            }
        }
        tasks.clear();
        return true;
    }

    /**
     * Удаление всех Epic и их Subtask
     **/
    @Override
    public boolean removeAllEpics() throws ManagerSaveException {
        if (!epics.isEmpty()) { // если HashMap с epics не пустая

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
        epics.clear();  // очищаем HashMap с эпиками
        subtasks.clear(); // очищаем HashMap с subtask
        return true;
    }

    /**
     * Удаление всех Subtask
     */
    @Override
    public boolean removeAllSubtasks() throws ManagerSaveException {
        if (!subtasks.isEmpty()) {   // если HashMap с subtasks не пустая

            for (Subtask subtask : subtasks.values()) {
                historyManager.removeFromHistory(subtask.getId());
                subtask.clearIDForSubtask();
                tasksTreeSet.remove(subtask);
            }

            for (Epic epic : epics.values()) {
                ArrayList<Integer> isSubtask = epic.getAllSubtask();
                isSubtask.clear();
                calcStatus(epic);
                calcTimeForEpic(epic);
            }
            subtasks.clear(); // очищаем HashMap с subtask
        }
        return true;
    }

    /**
     * Удаление Task по идентификатору.
     *
     * @return статус операции true/false
     **/
    @Override
    public boolean removeByIdTask(Integer id) throws ManagerSaveException {
        Task task = tasks.remove(id);
        historyManager.removeFromHistory(id);// при удалении задачи она также удаляется из истории просмотров
        if (task != null) task.clearID();
        tasksTreeSet.remove(task);
        return task != null;
    }

    /**
     * Удаление Epic по идентификатору и удаление его Subtask
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
            tasksTreeSet.remove(removeSubtask);
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
    public boolean removeSubtaskById(Integer idSubtask) throws ManagerSaveException {
        if (idSubtask == null) return false;
        Subtask removeSubtask = subtasks.get(idSubtask);
        if (removeSubtask == null) return false;
        Integer idEpic = removeSubtask.getIdEpic();
        if (idEpic == null) return false;
        Epic epic = epics.get(idEpic);
        epic.getAllSubtask().remove(removeSubtask.getId());
        subtasks.remove(removeSubtask.getId());
        tasksTreeSet.remove(removeSubtask);
        calcStatus(epic);

        historyManager.removeFromHistory(idSubtask);  // при удалении задачи она также удаляется из истории просмотров
        removeSubtask.clearID();
        return true;
    }
}



