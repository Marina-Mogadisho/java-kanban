package managers;

public class Managers {
    private static String filename4save = "saveDATA.txt";

    public static TaskManager getDefault() {
        /*
        HistoryManager historyManager = new InMemoryHistoryManager() -- создается объект класса InMemoryHistoryManager()
        но его переменная типа HistoryManager, так как HistoryManager для него родительский класс
         */

        /* в конструкторе класса FileBackedTaskManager() передается ссылка на класс HistoryManager(), что бы тот
        мог использовать его методы по сохранению Истории просмотра задач.
        А также имя файла, где будут хранится все задачи при использовании методов добавления, изменения
         */
        //FileBackedTaskManager manager = new FileBackedTaskManager(historyManager, fileName);

        //manager.load(); // метод load()  загружает данные из файла
        // return manager


        // в результате создался объект класса FileBackedTaskManager и в него загрузили данные, которые были в файле
        return FileBackedTaskManager.loadFromFile(filename4save);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static void setFileNameForSave(String filename4save1) {
        filename4save = filename4save1;
    }
}
