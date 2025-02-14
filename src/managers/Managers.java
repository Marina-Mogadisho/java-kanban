package managers;

public class Managers {
    private static String filename4save = "saveDATA.txt";

    public static TaskManager getDefault() {
        //HistoryManager historyManager = getDefaultHistory();
        //return new InMemoryTaskManager(historyManager);
        return FileBackedTaskManager.loadFromFile(filename4save);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static void setFileNameForSave(String filename4save1) {
        filename4save = filename4save1;
    }
}
