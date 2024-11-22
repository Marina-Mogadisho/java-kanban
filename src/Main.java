import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
      /*  Создайте две задачи, а также эпик с двумя подзадачами и эпик с одной подзадачей.
        Распечатайте списки эпиков, задач и подзадач через System.out.println(..).
        Измените статусы созданных объектов, распечатайте их.
        Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.

        Попробуйте удалить одну из задач и один из эпиков.
      */


        //?????????????? советуем применить знания о методах equals() и hashCode(), чтобы реализовать идентификацию задачи по её id.
        // При этом две задачи с одинаковым id должны выглядеть для менеджера как одна и та же.
        //💡 Эти методы нежелательно переопределять в наследниках. Ваша задача — подумать, почему.
        //надо поменять метод setadd, часть вынести
        //     поменьше кода сделать в Task и Epic и побольше в Taskmenedger
        // переименовать add в create
        //переделать set индификатор


//  **** Тестирование ****
//////////////////////////

        TaskManager manager = new TaskManager();
        // тестовые данные добавляемые в менеджер
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description task 2", Status.NEW);
        Epic epic1 = new Epic("Epic 1", "Description epic 1");
        Epic epic2 = new Epic("Epic 2", "Description epic 2");
        Epic epic3 = new Epic("Epic 3", "Description epic 3");

        Subtask subtask1_Epic1 = new Subtask("Subtask 1", "Description subtask 1", Status.NEW);
        Subtask subtask2_Epic1 = new Subtask("Subtask 2", "Description subtask 2", Status.DONE);
        Subtask subtask1_Epic2 = new Subtask("Subtask 1", "Description subtask 1", Status.IN_PROGRESS);
        Subtask subtask1_Epic3 = new Subtask("Subtask 1", "Description subtask 2", Status.DONE);
        Subtask subtask2_Epic3 = new Subtask("Subtask 2", "Description subtask 2", Status.DONE);


        manager.addTask(task1);// добавляем Task
        manager.addTask(task2);// добавляем Task
        manager.addEpic(epic1);// добавляем Epic
        manager.addEpic(epic2);
        manager.addEpic(epic3);
        manager.addSubtaskInEpic(epic1.getId(), subtask1_Epic1);
        manager.addSubtaskInEpic(epic1.getId(), subtask2_Epic1);
        manager.addSubtaskInEpic(epic2.getId(), subtask1_Epic2);
        manager.addSubtaskInEpic(epic3.getId(), subtask1_Epic3);
        manager.addSubtaskInEpic(epic3.getId(), subtask2_Epic3);


        System.out.println();
        System.out.println("Получение задачи по идентификатору (id = " + task2.getId() + "):");
        System.out.println(manager.getById(2));
        System.out.println();

        System.out.println("Получение списка всех Task:");
        System.out.println(manager.getListAllTasks());
        System.out.println();

        System.out.println("Получение списка всех Epic:");
        System.out.println(manager.getListAllEpic());
        System.out.println();

        System.out.println("Получение одного конкретного Subtask по id Epic и id Subtask:");
        System.out.println("Сначала выводим Epic, его id " + epic1.getId());
        System.out.println(epic1);
        System.out.println("Потом Subtasks (id " + subtask1_Epic1.getId() + ", " + subtask2_Epic1.getId() + ")");
        System.out.println(manager.getSubtaskForEpicId(epic1.getId(), subtask1_Epic1.getId()));
        System.out.println(manager.getSubtaskForEpicId(epic1.getId(), subtask2_Epic1.getId()));
        System.out.println();

        System.out.println("Получение списка Subtask по id Epic = " + epic1.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println("Получение списка Subtask по id Epic = " + epic2.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic2.getId()));
        System.out.println("Получение списка Subtask по id Epic = " + epic3.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic3.getId()));
        System.out.println();

        System.out.println("Вызов метода: Удаление Task по идентификатору (id = " + task2.getId() + ").");
        manager.removeByIdTask(task2.getId());
        System.out.println("Получение задачи по идентификатору (id = " + task2.getId() + "):");
        System.out.println(manager.getById(2));
        System.out.println();
        System.out.println("Получение списка всех Task:");
        System.out.println(manager.getListAllTasks());
        System.out.println();


        System.out.println("Вызов метода: Удаление Epic по идентификатору (id = " + epic3.getId() + ").");
        manager.removeByIdEpic(epic3.getId());
        System.out.println("Получение списка всех Epic:");
        ArrayList<Task> list_all_epic = manager.getListAllEpic();
        for(Task t:list_all_epic)System.out.println(t);
        System.out.println();


        System.out.println("Получение списка Subtask по id Epic = " + epic3.getId());
        ArrayList<Subtask> list_subtask3 = manager.getListAllSubtaskForEpicId(epic3.getId());
        if(list_subtask3!=null)System.out.println(manager.getListAllSubtaskForEpicId(epic3.getId()));
        else System.out.println("нет Epic id:"+epic3.getId());

        System.out.println();

        System.out.println("Вызов метода: Удаление Subtask по идентификатору Subtask (id = " + subtask1_Epic1.getId() + ").");
        manager.removeByIdSubtask(subtask1_Epic1.getId());
        System.out.println("Получение списка Subtask по id Epic = " + epic1.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));






        System.out.println("Вызов метода: Удаление всех Task.");
        manager.removeAllTasks();
        System.out.println("Получение списка всех Task:");
        System.out.println(manager.getListAllTasks());
        System.out.println();



        System.out.println("Вызов метода: Удаление всех Epic.");
        manager.removeAllEpic();
        System.out.println("Получение списка всех Epic:");
        System.out.println(manager.getListAllEpic());
        System.out.println("Получение списка Subtask по id Epic = " + epic1.getId());
        System.out.println(manager.getListAllSubtaskForEpicId(epic1.getId()));
        System.out.println();

        ///??????????? Программа выдает ошибку при запросе вывода Subtask, если их нет в коллекции




/*
        // 1) Получили id  из задачи task2.getId() 2) getById() получение задачи по id
        Integer id1 = task1.getId();
        Task task = manager.getById(id1);
        System.out.println(task);
        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);




        // manager.addTask(task2);// добавляем уже существующий Task те делаем UPDATE

        // Integer id_task1=task1.getId();// получили идентификатор Task
        //manager.addSubtaskInEpic(id_task1,subtask1);// попытка добавить Subtask в Task

        // Integer id_epic1=epic1.getId(); // получили идентификатор Epic
        // manager.addSubtaskInEpic(id_epic1,subtask1); // добавили Subtask в Epic

        //Integer id_epic2=epic1.getId(); // получили идентификатор Epic
        // manager.addSubtaskInEpic(id_epic2,subtask2); // добавили Subtask в Epic


        // Status epic1Status = epic1.getStatus();
        // System.out.println("Статус Epic 1 " + epic1Status);


        // Task epic=manager.getById(epic1.getId());
        //System.out.println(epic);
*/
    }
}
