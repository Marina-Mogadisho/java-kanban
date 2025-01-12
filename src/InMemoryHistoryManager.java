import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final MyLinkedList historyList;

    public InMemoryHistoryManager() {
        this.historyList = new MyLinkedList(); // Создаю список, в кот. будут храниться ссылки на задачи, кот.вызывались
        // т.е.создается мапа с ключом id задачи и значением узлом, и еще поле с последним узлом цепочки
    }

    /**
     * Метод добавления задачи(подзадачи, эпика) в историю просмотров:
     * в созданный мною список historyList типа MyLinkedList, где создается узел и добавляется задача
     */
    @Override
    public void addHistory(Task task) {
        historyList.addTaskAndCreateNode(task);
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
     * @return ArrayList<>()
     **/
    @Override
    public List<Task> getHistory() {
        return historyList.listNode();
    }
}
