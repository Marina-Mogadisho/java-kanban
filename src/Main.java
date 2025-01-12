public class Main {
    public static void main(String[] args) {

        System.out.println();
        System.out.println(" ***  Тестирование программы  ***");
        System.out.println();

        // ПРОВЕРЯЕМ ДОБАВЛЕНИЕ ЗАДАЧ
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Task 1", "Description task 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description task 2", Status.IN_PROGRESS);

        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        Epic epic2 = new Epic("Epic 2", "Description epic 2");
        //Epic epic3 = new Epic("Epic 3", "Description epic 3");

        manager.addTask(task1);
        manager.addTask(task2);

        manager.addEpic(epic1);
        manager.addEpic(epic2);
       // manager.addEpic(epic3);

        Subtask subtask1_Epic1 = new Subtask(epic1.getId(), "Subtask 1", "Description subtask 1", Status.NEW);
        Subtask subtask2_Epic1 = new Subtask(epic1.getId(), "Subtask 2", "Description subtask 2", Status.DONE);
        Subtask subtask3_Epic1 = new Subtask(epic1.getId(), "Subtask 3", "Description subtask 3", Status.DONE);
        manager.addSubtask(subtask1_Epic1);
        manager.addSubtask(subtask2_Epic1);
        manager.addSubtask(subtask3_Epic1);

        System.out.println();
        System.out.println("*** ПРОВЕРЯЕМ ПОЛУЧЕНИЕ ЗАДАЧ ***");
        manager.getTaskById(task2.getId());
        manager.getTaskById(task1.getId());

        manager.getEpicById(epic1.getId());
        System.out.println(manager.getHistory());

        manager.getSubtaskById(subtask3_Epic1.getId());
        manager.getSubtaskById(subtask2_Epic1.getId());
        manager.getSubtaskById(subtask1_Epic1.getId());

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
        Subtask subtask1_Epic2 = new Subtask(epic2.getId(), "Subtask 1", "Description subtask 1", Status.IN_PROGRESS);
        Subtask subtask1_Epic3 = new Subtask(epic3.getId(), "Subtask 1", "Description subtask 2", Status.DONE);
        Subtask subtask2_Epic3 = new Subtask(epic3.getId(), "Subtask 2", "Description subtask 2", Status.DONE);
        manager.addSubtask(subtask1_Epic2);
        manager.addSubtask(subtask1_Epic3);
        manager.addSubtask(subtask2_Epic3);

        System.out.println("добавляем Task 1 " + manager.addTask(task1));
        System.out.println("добавляем Task 2 " + manager.addTask(task2));
        System.out.println("добавляем Epic 1 " + manager.addEpic(epic1));
        System.out.println("добавляем Epic 2 " + manager.addEpic(epic2));
        System.out.println("добавляем Epic 3 " + manager.addEpic(epic3));
        System.out.println("добавляем Subtask 1 в Epic 1 " + manager.addSubtask(subtask1_Epic1));
        System.out.println("добавляем Subtask 2 в Epic 1 " + manager.addSubtask(subtask2_Epic1));
        System.out.println("добавляем Subtask 1 в Epic 2 " + manager.addSubtask(subtask1_Epic2));
        System.out.println("добавляем Subtask 1 в Epic 3 " + manager.addSubtask(subtask1_Epic3));
        System.out.println("добавляем Subtask 1 в Epic 3 " + manager.addSubtask(subtask2_Epic3));

        //*** ПРОВЕРЯЕМ ПОЛУЧЕНИЕ ЗАДАЧ ***

        System.out.println(manager.getHistory());
        manager.getEpicById(epic1.getId());
        System.out.println(manager.getHistory());
        manager.getSubtaskById(subtask3_Epic1.getId());
        manager.getSubtaskById(subtask2_Epic1.getId());
        System.out.println(manager.getHistory());

        manager.getTaskById(task2.getId());
        System.out.println();
        System.out.println(manager.getHistory());

        manager.getEpicById(epic2.getId());
        manager.getEpicById(epic1.getId());
        System.out.println();
        System.out.println(manager.getHistory());

       // manager.getSubtaskById(subtask2_Epic1.getId());
        System.out.println();
        System.out.println("Список после удаления задачи:");
        manager.removeByIdTask(task2.getId());
        System.out.println(manager.getHistory());
        System.out.println("Получение задачи по идентификатору (id = " + task2.getId() + "):");
        System.out.println(manager.getTaskById(2));
        System.out.println();

        System.out.println("Получение списка всех Task:");
        System.out.println(manager.getListAllTasks());
        System.out.println();

        System.out.println("Получение списка всех Epic:");
        System.out.println(manager.getListAllEpic());
        System.out.println();

        System.out.println("Получение задачи Epic по идентификатору (id = " + epic1.getId() + "):");
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();

        System.out.println("Получение подзадачи Subtask по идентификатору id=" + subtask2_Epic1.getId());
        System.out.println(manager.getSubtaskById(subtask2_Epic1.getId()));
        System.out.println();


        System.out.println("Получение списка последних 10 вызванных задач");
        System.out.println(manager.getHistory());
        System.out.println("Получение списка всех Subtask:");
        System.out.println(manager.getListAllSubtask());
        System.out.println();

        System.out.println("Получение списка Subtask по id Epic = " + epic1.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println("Получение списка Subtask по id Epic = " + epic2.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic2.getId()));
        System.out.println("Получение списка Subtask по id Epic = " + epic3.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic3.getId()));
        System.out.println();

       // *** ПРОВЕРЯЕМ МЕТОДЫ ОБНОВЛЕНИЯ ***

        System.out.println("***  ПРОВЕРЯЕМ МЕТОДЫ ОБНОВЛЕНИЯ  ***");

        System.out.println("Обновление Task");
        System.out.println("Получение старого Task, его id = " + task2.getId() + ":");
        System.out.println(manager.getTaskById(task2.getId()));
        Task task22 = new Task("Task new_2", "Description task new_2", Status.DONE);
        task22.setId(task2.getId());
        System.out.println("Обновление Task 2 " + manager.updateTask(task22));
        System.out.println(manager.getTaskById(task22.getId()));

        System.out.println();
        System.out.println("Обновление Epic");
        System.out.println("Получение старого Epic, его id = " + epic3.getId() + ":");
        System.out.println(manager.getEpicById(epic3.getId()));
        Epic epic33 = new Epic("Epic new_3", "Description epic new_3");
        epic33.setId(epic3.getId()); // положили id старого Epic в новый
        System.out.println("Обновление Epic 3 " + manager.updateEpic(epic33));
        System.out.println(manager.getEpicById(epic3.getId()));
        System.out.println();

        System.out.println("Oбновлениe Subtask и обновление статуса Epic");
        System.out.println("Получение старого Subtask (id= " + subtask2_Epic1.getId() + ") и его Epic (id= " + epic1.getId() + "):");
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println(manager.getEpicById(epic1.getId()));
        Subtask subtask22_Epic1 = new Subtask(epic1.getId(),"Subtask new_2", "Description subtask new_2", Status.NEW);
        subtask22_Epic1.setId(subtask2_Epic1.getId());
        System.out.println("Обновление subtask2_Epic1 " + manager.updateSubtaskAndEpic(subtask22_Epic1));
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println();
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();

        //  *** ПРОВЕРЯЕМ МЕТОДЫ УДАЛЕНИЯ

        System.out.println();
        System.out.println(" *** ПРОВЕРЯЕМ МЕТОДЫ УДАЛЕНИЯ");

        System.out.println("Получение списка всех Task:");
        System.out.println(manager.getListAllTasks());
        System.out.println();
        System.out.println("Удаление Task 2 " + manager.removeByIdTask(task2.getId()));
        System.out.println(manager.getTaskById(task2.getId()));
        System.out.println();
        System.out.println("Получение списка всех Task:");
        System.out.println(manager.getListAllTasks());
        System.out.println();
        System.out.println("Удаление всех Task");
        manager.removeAllTasks();
        System.out.println("Получение списка всех Task:");
        System.out.println(manager.getListAllTasks());
        System.out.println();


        System.out.println("Вызов метода: Удаление Epic по идентификатору (id = " + epic3.getId() + ").");
        manager.removeByIdEpic(epic3.getId());
        System.out.println("Получение списка всех Epic:");
        System.out.println(manager.getListAllEpic());
        System.out.println();
        System.out.println("Удаление  Epic 2 по идентификатору (id = " + epic2.getId() + ")");
        System.out.println(manager.removeByIdEpic(epic2.getId()));
        System.out.println("Получение списка всех Epic:");
        System.out.println(manager.getListAllEpic());
        System.out.println();
        System.out.println("Вызов метода: Удаление всех Epic.");
        manager.removeAllEpics();
        System.out.println("Получение списка всех Epic:");
        System.out.println(manager.getListAllEpic());
        System.out.println("Получение списка Subtask по id Epic = " + epic1.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println();

        System.out.println("Получение задачи Epic по идентификатору (id = " + epic1.getId() + "):");
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();


        System.out.println("Получение списка Subtask по id Epic = " + epic1.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println("Получение задачи Epic по идентификатору (id = " + epic1.getId() + "):");
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();

        System.out.println("Вызов метода: Удаление Subtask по идентификатору Subtask (id = " + subtask1_Epic1.getId() + ").");
        System.out.println(manager.removeSubtaskById(subtask1_Epic1.getId()));
        System.out.println();


        System.out.println("Вызов метода: Удаление всех  Subtask.");
        System.out.println(manager.removeAllSubtasks());
        System.out.println();
        System.out.println("Получение списка всех Subtask:");
        System.out.println(manager.getListAllSubtask());
        System.out.println();

        System.out.println("Получение задачи Epic по идентификатору (id = " + epic1.getId() + "):");
        System.out.println(manager.getEpicById(epic1.getId()));
        System.out.println();
        System.out.println("Получение списка Subtask по id Epic = " + epic1.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
*/
    }
}

