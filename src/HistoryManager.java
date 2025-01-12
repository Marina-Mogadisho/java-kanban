import java.util.List;

public interface HistoryManager {

    void addHistory(Task task);

    void removeFromHistory(Integer id);

    List<Task> getHistory();

}
