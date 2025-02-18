import managers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;
import util.UtilTime;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @BeforeEach
    public void init() throws IOException {
        //Создает пустой файл в каталоге временных файлов по умолчанию
        File file = File.createTempFile("filecfg", ".txt");
        String newFile = file.getAbsolutePath();  // Возвращает строку абсолютного пути этого абстрактного пути.
        manager = FileBackedTaskManager.loadFromFile(newFile);
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
    }


    @Test
    void testSave() throws IOException,IntersectionTaskException {
        HistoryManager historyManager = manager.getHistoryManager();

        // Создает пустой файл в каталоге временных файлов по умолчанию
        File file = File.createTempFile("filecfg", ".txt");
        String newFile = file.getAbsolutePath();  // Возвращает строку абсолютного пути этого абстрактного пути.
        FileBackedTaskManager fileTaskManager = new FileBackedTaskManager(historyManager, newFile);
        fileTaskManager.save();  // возникнет пустой файл

        boolean ex = file.exists();  //Проверяет, существует ли файл или каталог, обозначенный этим абстрактным путем.
        assertTrue(ex, "Файл не существует");

         /*
        сохранение и загрузку пустого файла;
        сохранение нескольких задач;
        загрузку нескольких задач.
         */
        Task task1 = new Task("Task 1", "Description task 1", Status.NEW,
                UtilTime.stringOfDuration("10"), UtilTime.stringOfLocalTime("10:50 14.02.2025"));
        fileTaskManager.addTask(task1);
        Epic epic1 = new Epic("tasks.Epic 1", "Description epic 1");
        fileTaskManager.addEpic(epic1);

        FileBackedTaskManager testFileTaskManager = new FileBackedTaskManager(historyManager, newFile);
        testFileTaskManager.load();

        List<Task> taskList = testFileTaskManager.getListAllTasks();
        int taskListSize = taskList.size();
        assertEquals(1, taskListSize, "Задачи не добавлены в файл");

        List<Epic> epicList = testFileTaskManager.getListAllEpic();
        int epicListSize = epicList.size();
        assertEquals(1, epicListSize, "Эпик не добавлен в файл");
    }

    @Test
    void testException() {

        Assertions.assertThrows(ManagerSaveException.class, () -> {
            HistoryManager historyManager = manager.getHistoryManager();
            FileBackedTaskManager fileTaskManager = new FileBackedTaskManager(historyManager, "z:\\tetetet.txt");
            fileTaskManager.save();  // возникнет пустой файл
            throw new ManagerSaveException("Ошибка теста ==> testException");
        }, "Тест-ошибка. Исключения не произошло");
    }

/*
        assertThrows(ManagerSaveException.class, () -> {
            try {
                HistoryManager historyManager = manager.getHistoryManager();
                FileBackedTaskManager fileTaskManager = new FileBackedTaskManager(historyManager, "z:\\tetetet.txt");
                fileTaskManager.save();  // возникнет пустой файл
                throw new ManagerSaveException("Ошибка теста ==> testException");
            } catch (ManagerSaveException e) {
                System.out.println("Тест-ошибка. Исключения не произошло.  " + e.getMessage());
            }
        });
    }

 */
}