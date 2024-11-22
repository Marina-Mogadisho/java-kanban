import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtasks;

    public Epic(String title, String description) {
        // конструктор родителя Task, который устанавливает параметры и статус
        super(title, description, Status.NEW);
        subtasks = new HashMap<>(); // создаем коллекцию для подзадач, ссылку на коллекцию храним в subtasks
    }


    /**
     * если у Epic попытаться изменить статус, то ничего не произойдет, выполнится пустая команда
     */
    @Override
    public void setStatus(Status status) {
    }

    /**
     * Метод добавления подзадачи в Epic
     */
    @Override
    public void addSubtask(Subtask subtask) {
        Integer new_id_subtask = TaskManager.getId(); // // увеличили счетчик newid++ и присвоили id
        subtask.setId(new_id_subtask); // добавили id в поле объекта задачи
        subtasks.put(new_id_subtask, subtask);//положили задачу в коллекцию с подзадачами, HashMap  subtasks
    }

    /**
     * Определяем статус Epic, через статусы подзадач
     */
    @Override
    public Status getStatus() {
        //Status st = super.getStatus();
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
            return Status.NEW;
        }
        else {
            boolean is_done = true;
            for (Subtask subtask : subtasks.values()) {
                Status status = subtask.getStatus();
                // проверяем что все элементы
                if (status != Status.DONE) {
                    is_done = false;
                    break;
                }
            }
            if (is_done) return Status.DONE;
            else {
                return Status.IN_PROGRESS;
            }
        }
    }

    public HashMap<Integer, Subtask> getAllSubtask() {
        return subtasks;
    }
}
