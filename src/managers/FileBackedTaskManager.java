package managers;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {


    private final String fileName;

    public FileBackedTaskManager(HistoryManager historyManager, String fileName) {
//Пусть новый менеджер получает файл для авто сохранения в своём конструкторе и сохраняет его в поле.
        super(historyManager);
        this.fileName = fileName;
    }


    /**
     * Метод loadFromFile(String fileName)
     * 1. Создается объект  HistoryManager
     * 2. Создается объект FileBackedTaskManager
     * 3. Выполняется метод load
     */
    public static TaskManager loadFromFile(String fileName) {
        HistoryManager historyManager = Managers.getDefaultHistory();
        FileBackedTaskManager manager = new FileBackedTaskManager(historyManager, fileName);
        manager.load();
        return manager;
    }

    /**
     * Метод восстанавливает данные менеджера из файла при запуске программы
     * Метод "Десериализации"
     */
    public void load() {
        /*
        New FileReader - Создает новый объект класса FileReader, учитывая имя файла, из которого выполняется чтение (fileName).
        И в нужной кодировке UTF_8.
        New BufferedReader - Открывает поток и буферизует ввод данных из указанного файла.
         */
        try (BufferedReader br = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            while (true) {
                String lineTask = br.readLine();   // читает строку текста из потока, текст до возврата каретки (\n)
                if (lineTask == null) break;
                String[] split = lineTask.split(","); //Разбивает данную строку на части, до запятой один элемент
                //Создали массив из элементов строки
                if (split == null)
                    continue;  //если массив со считанными данными пуст, возвращаемся на новый круг цикла while (true)
                if (split.length < 2)
                    continue;  // если в массиве только один элемент, возвращаемся на новый круг цикла while (true)
                if (split[1].equals("TASK")) {  // если второй элемент равен слову "TASK"
                    Integer id = checkTaskAndId(split); // проверяем, что в TASK нужное количество параметров и id>0
                    String startTime = null;
                    String duration = "0";
                    if (split.length > 5) {
                        startTime = split[5];
                    }
                    if (split.length > 6) {
                        duration = split[6];
                    }
                    Task task = new Task(split[2], split[4], Status.valueOf(split[3]), duration, startTime);
                    task.setId(id); // добавили id в task
                    getTasks().put(id, task);
                } else if (split[1].equals("EPIC")) {
                    Integer id = checkTaskAndId(split);// проверяем, что в EPIC нужное количество параметров и id>0
                    Epic epic = new Epic(split[2], split[4]);
                    epic.setId(id);
                    getEpics().put(id, epic);

                } else if (split[1].equals("SUBTASK")) {
                    if (split.length < 6) continue;
                    Integer id;
                    Integer idEpic;
                    try {
                        id = Integer.parseInt(split[0]);
                        idEpic = Integer.parseInt(split[7]);
                    } catch (Exception e) {
                        id = -1;
                        idEpic = -1;
                    }
                    if (id < 0 || idEpic < 0) continue;
                    setNextId(id);
                    setNextId(idEpic);
                    //public Subtask(Integer idEpic, String title, String description, Status status)
                    String startTime = null;
                    String duration = "0";
                    if (split.length > 5) {
                        startTime = split[5];
                    }
                    if (split.length > 6) {
                        duration = split[6];
                    }
                    Subtask subtask = new Subtask(idEpic, split[2], split[4], Status.valueOf(split[3]), duration, startTime);
                    subtask.setId(id);
                    getSubtasks().put(id, subtask);
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
    }

    /**
     * Метод проверяет, что скаченная из файла строка имеет длину не менее 5 элементов, и что id не меньше 0
     * подходит для проверки "TASK" и "EPIC", так как у них 5 параметров
     * {id, type, name, status, description}
     */
    private Integer checkTaskAndId(String[] split) {
        Integer id = 0;
        try {
            // в"TASK" и "EPIC" должно быть 5 параметров, значит и длина д.б. = 5
            if (split.length < 5) {
                throw new ManagerLoadException("При считывании из файла не хватает параметров задачи");
            }
            // если в параметрах не задан id
            id = Integer.parseInt(split[0]);  // внутри parseInt() генерируется исключение throw new NumberFormatException
            if (id < 0) {
                throw new ManagerLoadException("При считывании из файла не указан номер id");
            }
        } catch (Exception e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        setNextId(id);
        return id;
    }

    /**
     * Метод будет сохранять текущее состояние менеджера (список задач с параметрами)в указанный файл.
     * Метод СЕРИАЛИЗАЦИИ
     */
    public void save() throws ManagerSaveException {
        //создаем объект типа FileWriter, чтобы можно было писать в файл, имя которого указано и в кодировке UTF_8
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8))) {
            bw.write("id,type,name,status,description,startTime,duration,epic");
            bw.newLine();

            for (Task task : getTasks().values()) {
                String line = task.toString();

                bw.write(line);
                bw.newLine();  // запись разделителя строки, чтобы следующее слово записалось на новую строку
            }

            for (Subtask subtask : getSubtasks().values()) {
                String line = subtask.toString();
                bw.write(line);
                bw.newLine();  // запись разделителя строки, чтобы следующее слово записалось на новую строку
            }

            for (Epic epic : getEpics().values()) {
                String line = epic.toString();
                bw.write(line);
                bw.newLine();  // запись разделителя строки, чтобы следующее слово записалось на новую строку
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла: " + fileName);
        }
    }


    @Override
    public boolean addTask(Task newTask) throws ManagerSaveException {
        if (!super.addTask(newTask)) return false;
        save();
        return true;
    }

    @Override
    public boolean addSubtask(Subtask newSubtask) throws ManagerSaveException {
        if (!super.addSubtask(newSubtask)) return false;
        save();
        return true;
    }

    @Override
    public boolean addEpic(Epic newEpic) throws ManagerSaveException {
        if (!super.addEpic(newEpic)) return false;
        save();
        return true;
    }

    @Override
    public Task getTaskById(Integer id) throws ManagerSaveException {
        Task task = super.getTaskById(id);
        if (task == null) return null;
        save();
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) throws ManagerSaveException {
        Epic epic = super.getEpicById(id);
        if (epic == null) {
            return null;
        }
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) throws ManagerSaveException {
        Subtask subtask = super.getSubtaskById(id);
        if (subtask == null) return null;
        save();
        return subtask;
    }

    @Override
    public ArrayList<Task> getListAllTasks() throws ManagerSaveException {
        ArrayList<Task> tasks = super.getListAllTasks();
        if (tasks == null) return null;
        save();
        return tasks;
    }

    @Override
    public ArrayList<Epic> getListAllEpic() throws ManagerSaveException {
        ArrayList<Epic> epics = super.getListAllEpic();
        if (epics == null) return null;
        save();
        return epics;
    }

    @Override
    public ArrayList<Subtask> getListAllSubtask() throws ManagerSaveException {
        ArrayList<Subtask> subtasks = super.getListAllSubtask();
        if (subtasks == null) return null;
        save();
        return subtasks;
    }

    @Override
    public List<Subtask> getListAllSubtaskForEpicId(Integer idEpic) throws ManagerSaveException {
        List<Subtask> allSubtaskForEpicId = super.getListAllSubtaskForEpicId(idEpic);
        if (allSubtaskForEpicId == null) return null;
        save();
        return allSubtaskForEpicId;
    }

    @Override
    public boolean updateTask(Task newTask) throws ManagerSaveException {
        if (!super.updateTask(newTask)) return false;
        save();
        return true;
    }

    @Override
    public boolean updateSubtaskAndEpic(Subtask newSubtask) throws ManagerSaveException {
        if (!super.updateSubtaskAndEpic(newSubtask)) return false;
        save();
        return true;
    }

    @Override
    public boolean updateEpic(Epic newEpic) throws ManagerSaveException {
        if (!super.updateEpic(newEpic)) return false;
        save();
        return true;
    }

    @Override
    public boolean removeAllTasks() throws ManagerSaveException {
        if (!super.removeAllTasks()) return false;
        save();
        return true;
    }

    @Override
    public boolean removeAllEpics() throws ManagerSaveException {
        if (!super.removeAllEpics()) return false;
        save();
        return true;
    }

    @Override
    public boolean removeAllSubtasks() throws ManagerSaveException {
        if (!super.removeAllSubtasks()) return false;
        save();
        return true;
    }

    @Override
    public boolean removeByIdTask(Integer id) throws ManagerSaveException {
        if (!super.removeByIdTask(id)) return false;
        save();
        return true;
    }

    @Override
    public boolean removeByIdEpic(Integer id) throws ManagerSaveException {
        if (!super.removeByIdEpic(id)) return false;
        save();
        return true;
    }

    @Override
    public boolean removeSubtaskById(Integer idSubtask) throws ManagerSaveException {
        if (!super.removeSubtaskById(idSubtask)) return false;
        save();
        return true;
    }
}
