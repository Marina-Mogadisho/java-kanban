import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> historyList;
    private final int maxSizeList;

    public InMemoryHistoryManager(int size) {
        this.historyList = new ArrayList<>(); // создаю список, в кот. будут храниться ссылки на задачи, кот.вызывались
        this.maxSizeList = size; // Максимальный размер списка ArrayList<T> historyList
    }

    /**
     * Метод добавления задачи(подзадачи, эпика) в список  ArrayList<T> historyList
     */
    @Override
    public void addHistory(Task task) {
        if (historyList.size() >= maxSizeList) {
            historyList.removeFirst();
        }
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
