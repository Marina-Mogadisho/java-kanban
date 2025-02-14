package tasks;

import java.util.Comparator;

public class CompareTask implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        if (t1 == null || t2 == null) return 0;
        if (t1.getStartTime() == null && t2.getStartTime() == null) return 0;
        if (t1.getStartTime() == null) return -1;
        if (t2.getStartTime() == null) return 1;
        return t1.getStartTime().compareTo(t2.getStartTime());
    }
}
