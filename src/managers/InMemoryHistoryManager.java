package managers;

import tasks.Task;
import util.MyLinkedList;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final MyLinkedList historyList;

    public InMemoryHistoryManager() {
        // Создаю список, в котором будут храниться ссылки на задачи, которые вызывались
        // т.е.создается мапа с ключом id задачи и значением узлом, и еще поле с последним узлом цепочки
        this.historyList = new MyLinkedList();
    }

    /**
     * Метод добавления задачи(подзадачи, эпика) в историю просмотров:
     * в созданный мною список historyList типа util.MyLinkedList, где создается узел и добавляется задача
     */
    @Override
    public void addHistory(Task task) {
        if (task != null) {
            historyList.addTaskAndCreateNode(task);
        } else {
            System.out.println("Ошибка при добавлении задачи в историю просмотров. Пришла задача без аргументов.");
        }
    }

    /**
     * Метод удаления задачи из истории просмотров по ее id
     **/
    @Override
    public void removeFromHistory(Integer id) {
        Task task = historyList.getMapNode(id); // вызываем из узла поле с самой задачей
        if (task != null)
            historyList.removeTask(task); // вызываем метод удаления задачи из истории просмотров, также удаляется узел
    }

    /**
     * Метод возвращает список задач из истории просмотров в списке
     *
     * @return List<>()
     **/
    @Override
    public List<Task> getHistory() {
        return historyList.listNode();
    }
}
