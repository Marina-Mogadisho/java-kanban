import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {

        System.out.println();
        System.out.println(" ***  Тестирование программы  ***");
        System.out.println();

        // ПРОВЕРЯЕМ ДОБАВЛЕНИЕ ЗАДАЧ
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("tasks.Task 1", "Description task 1", Status.NEW);
        Task task2 = new Task("tasks.Task 2", "Description task 2", Status.IN_PROGRESS);

        Epic epic1 = new Epic("tasks.Epic 1", "Description epic 1");
        Epic epic2 = new Epic("tasks.Epic 2", "Description epic 2");
        //tasks.Epic epic3 = new tasks.Epic("tasks.Epic 3", "Description epic 3");

        manager.addTask(task1);
        manager.addTask(task2);

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        // manager.addEpic(epic3);

        Subtask subtask1Epic1 = new Subtask(epic1.getId(), "tasks.Subtask 1", "Description subtask 1", Status.NEW);
        Subtask subtask2Epic1 = new Subtask(epic1.getId(), "tasks.Subtask 2", "Description subtask 2", Status.DONE);
        Subtask subtask3Epic1 = new Subtask(epic1.getId(), "tasks.Subtask 3", "Description subtask 3", Status.DONE);
        manager.addSubtask(subtask1Epic1);
        manager.addSubtask(subtask2Epic1);
        manager.addSubtask(subtask3Epic1);

        System.out.println();
        System.out.println("*** ПРОВЕРЯЕМ ПОЛУЧЕНИЕ ЗАДАЧ ***");
        manager.getTaskById(task2.getId());
        manager.getTaskById(task1.getId());

        manager.getEpicById(epic1.getId());
        System.out.println(manager.getHistory());

        manager.getSubtaskById(subtask3Epic1.getId());
        manager.getSubtaskById(subtask2Epic1.getId());
        manager.getSubtaskById(subtask1Epic1.getId());

        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());

        System.out.println();
        System.out.println("История просмотров после удаления эпика с подзадачами:");
        manager.removeByIdEpic(epic1.getId());
        System.out.println(manager.getHistory());

        System.out.println();
        System.out.println("История просмотров после удаления задачи:");
        manager.removeByIdTask(task2.getId());
        System.out.println(manager.getHistory());


        /*
        tasks.Subtask subtask1_Epic2 = new tasks.Subtask(epic2.getId(), "tasks.Subtask 1", "Description subtask 1", tasks.Status.IN_PROGRESS);
        tasks.Subtask subtask1_Epic3 = new tasks.Subtask(epic3.getId(), "tasks.Subtask 1", "Description subtask 2", tasks.Status.DONE);
        tasks.Subtask subtask2_Epic3 = new tasks.Subtask(epic3.getId(), "tasks.Subtask 2", "Description subtask 2", tasks.Status.DONE);
        manager.addSubtask(subtask1_Epic2);
        manager.addSubtask(subtask1_Epic3);
        manager.addSubtask(subtask2_Epic3);

        System.out.println("добавляем tasks.Task 1 " + manager.addTask(task1));
        System.out.println("добавляем tasks.Task 2 " + manager.addTask(task2));
        System.out.println("добавляем tasks.Epic 1 " + manager.addEpic(epic1));
        System.out.println("добавляем tasks.Epic 2 " + manager.addEpic(epic2));
        System.out.println("добавляем tasks.Epic 3 " + manager.addEpic(epic3));
        System.out.println("добавляем tasks.Subtask 1 в tasks.Epic 1 " + manager.addSubtask(subtask1Epic1));
        System.out.println("добавляем tasks.Subtask 2 в tasks.Epic 1 " + manager.addSubtask(subtask2Epic1));
        System.out.println("добавляем tasks.Subtask 1 в tasks.Epic 2 " + manager.addSubtask(subtask1_Epic2));
        System.out.println("добавляем tasks.Subtask 1 в tasks.Epic 3 " + manager.addSubtask(subtask1_Epic3));
        System.out.println("добавляем tasks.Subtask 1 в tasks.Epic 3 " + manager.addSubtask(subtask2_Epic3));

        //*** ПРОВЕРЯЕМ ПОЛУЧЕНИЕ ЗАДАЧ ***

        System.out.println(manager.getHistory());
        manager.getEpicById(epic1.getId());
        System.out.println(manager.getHistory());
        manager.getSubtaskById(subtask3Epic1.getId());
        manager.getSubtaskById(subtask2Epic1.getId());
        System.out.println(manager.getHistory());

        manager.getTaskById(task2.getId());
        System.out.println();
        System.out.println(manager.getHistory());

        manager.getEpicById(epic2.getId());
        manager.getEpicById(epic1.getId());
        System.out.println();
        System.out.println(manager.getHistory());

       // manager.getSubtaskById(subtask2Epic1.getId());
        System.out.println();
        System.out.println("Список после удаления задачи:");
        manager.removeByIdTask(task2.getId());
        System.out.println(manager.getHistory());
        System.out.println("Получение задачи по идентификатору (id = " + task2.getId() + "):");
        System.out.println(manager.getTaskById(2));
        System.out.println();

        System.out.println("Получение списка всех tasks.Task:");
        System.out.println(manager.getListAllTasks());
        System.out.println();

        System.out.println("Получение списка всех tasks.Epic:");
        System.out.println(manager.getListAllEpic());
        System.out.println();

        System.out.println("Получение задачи tasks.Epic по идентификатору (id = " + epic1.getId() + "):");
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();
        System.out.println("Получение подзадачи tasks.Subtask по идентификатору id=" + subtask2Epic1.getId());
        System.out.println(manager.getSubtaskById(subtask2Epic1.getId()));
        System.out.println();


        System.out.println("Получение списка последних 10 вызванных задач");
        System.out.println(manager.getHistory());
        System.out.println("Получение списка всех tasks.Subtask:");
        System.out.println(manager.getListAllSubtask());
        System.out.println();

        System.out.println("Получение списка tasks.Subtask по id tasks.Epic = " + epic1.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println("Получение списка tasks.Subtask по id tasks.Epic = " + epic2.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic2.getId()));
        System.out.println("Получение списка tasks.Subtask по id tasks.Epic = " + epic3.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic3.getId()));
        System.out.println();

       // *** ПРОВЕРЯЕМ МЕТОДЫ ОБНОВЛЕНИЯ ***

        System.out.println("***  ПРОВЕРЯЕМ МЕТОДЫ ОБНОВЛЕНИЯ  ***");

        System.out.println("Обновление tasks.Task");
        System.out.println("Получение старого tasks.Task, его id = " + task2.getId() + ":");
        System.out.println(manager.getTaskById(task2.getId()));
        tasks.Task task22 = new tasks.Task("tasks.Task new_2", "Description task new_2", tasks.Status.DONE);
        task22.setId(task2.getId());
        System.out.println("Обновление tasks.Task 2 " + manager.updateTask(task22));
        System.out.println(manager.getTaskById(task22.getId()));

        System.out.println();
        System.out.println("Обновление tasks.Epic");
        System.out.println("Получение старого tasks.Epic, его id = " + epic3.getId() + ":");
        System.out.println(manager.getEpicById(epic3.getId()));
        tasks.Epic epic33 = new tasks.Epic("tasks.Epic new_3", "Description epic new_3");
        epic33.setId(epic3.getId()); // положили id старого tasks.Epic в новый
        System.out.println("Обновление tasks.Epic 3 " + manager.updateEpic(epic33));
        System.out.println(manager.getEpicById(epic3.getId()));
        System.out.println();

        System.out.println("Oбновлениe tasks.Subtask и обновление статуса tasks.Epic");
        System.out.println("Получение старого tasks.Subtask (id= " + subtask2Epic1.getId() + ") и его tasks.Epic (id= " + epic1.getId() + "):");
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println(manager.getEpicById(epic1.getId()));
        tasks.Subtask subtask22_Epic1 = new tasks.Subtask(epic1.getId(),"tasks.Subtask new_2", "Description subtask new_2", tasks.Status.NEW);
        subtask22_Epic1.setId(subtask2Epic1.getId());
        System.out.println("Обновление subtask2Epic1 " + manager.updateSubtaskAndEpic(subtask22_Epic1));
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println();
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();

        //  *** ПРОВЕРЯЕМ МЕТОДЫ УДАЛЕНИЯ

        System.out.println();
        System.out.println(" *** ПРОВЕРЯЕМ МЕТОДЫ УДАЛЕНИЯ");

        System.out.println("Получение списка всех tasks.Task:");
        System.out.println(manager.getListAllTasks());
        System.out.println();
        System.out.println("Удаление tasks.Task 2 " + manager.removeByIdTask(task2.getId()));
        System.out.println(manager.getTaskById(task2.getId()));
        System.out.println();
        System.out.println("Получение списка всех tasks.Task:");
        System.out.println(manager.getListAllTasks());
        System.out.println();
        System.out.println("Удаление всех tasks.Task");
        manager.removeAllTasks();
        System.out.println("Получение списка всех tasks.Task:");
        System.out.println(manager.getListAllTasks());
        System.out.println();


        System.out.println("Вызов метода: Удаление tasks.Epic по идентификатору (id = " + epic3.getId() + ").");
        manager.removeByIdEpic(epic3.getId());
        System.out.println("Получение списка всех tasks.Epic:");
        System.out.println(manager.getListAllEpic());
        System.out.println();
        System.out.println("Удаление  tasks.Epic 2 по идентификатору (id = " + epic2.getId() + ")");
        System.out.println(manager.removeByIdEpic(epic2.getId()));
        System.out.println("Получение списка всех tasks.Epic:");
        System.out.println(manager.getListAllEpic());
        System.out.println();
        System.out.println("Вызов метода: Удаление всех tasks.Epic.");
        manager.removeAllEpics();
        System.out.println("Получение списка всех tasks.Epic:");
        System.out.println(manager.getListAllEpic());
        System.out.println("Получение списка tasks.Subtask по id tasks.Epic = " + epic1.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println();

        System.out.println("Получение задачи tasks.Epic по идентификатору (id = " + epic1.getId() + "):");
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();


        System.out.println("Получение списка tasks.Subtask по id tasks.Epic = " + epic1.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println("Получение задачи tasks.Epic по идентификатору (id = " + epic1.getId() + "):");
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();

        System.out.println("Вызов метода: Удаление tasks.Subtask по идентификатору tasks.Subtask (id = " + subtask1Epic1.getId() + ").");
        System.out.println(manager.removeSubtaskById(subtask1Epic1.getId()));
        System.out.println();


        System.out.println("Вызов метода: Удаление всех  tasks.Subtask.");
        System.out.println(manager.removeAllSubtasks());
        System.out.println();
        System.out.println("Получение списка всех tasks.Subtask:");
        System.out.println(manager.getListAllSubtask());
        System.out.println();

        System.out.println("Получение задачи tasks.Epic по идентификатору (id = " + epic1.getId() + "):");
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();
        System.out.println("Получение списка tasks.Subtask по id tasks.Epic = " + epic1.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
*/
    }
}

