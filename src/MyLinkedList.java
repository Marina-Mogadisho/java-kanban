import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyLinkedList {
    private Node lastNode;  // последний добавленный элемент цепочки с узлами
    private final HashMap<Integer, Node> mapNode; //мапа: в ключе id, а в значении узел


    public MyLinkedList() {
        this.lastNode = null;
        this.mapNode = new HashMap<>();
    }

    public Task getMapNode(Integer id) {  // метод возвращает задачу из узла по ее id, у узла тот же id этой задачи
        Node node = mapNode.get(id);
        if (node == null) {
            //System.out.println("Узел пуст. НЕ могу вывести задачу c id= " + id);
            return null;
        } else {
            return node.task;
        }
    }

    public void addTaskAndCreateNode(Task task) {      //при добавлении задачи создается узел
        Node node = new Node(); //создаем объект тиа узел Node
        node.task = task;  // полю task в узле присваиваем значение аргумента (task), что передается на входе метода
        /*
        При добавлении в мапу, метод put()  возвращает значение oldNode, которое
        было по такому же ключу, а на его место записывает новое значение.
        Если такого ключа раньше не было, то метод возвращает null
        */
        Node oldNode = mapNode.put(task.getId(), node);

        if (oldNode != null) {
            removeNode(oldNode);   // удаляем старое значение oldNode из цепочки node
        }
        if (lastNode == null) {  // если последний узел пустой, значит и в цепочке ничего еще не было
            node.prev = null;  // поэтому поле, которое содержит ссылку на предыдущий узел равен null
            node.next = null; // следующий узел тоже null
            lastNode = node; //  а последним узлом становится пока единственный узел, который создали
        } else {   // если последний узел не пустой
            node.prev = lastNode; // то предыдущим узлом у созданного становится последний, который уже был в цепочке
            node.next = null; // следующий узел у созданного становится пустым
            lastNode.next = node;  // последующий у последнего становится созданный узел
            lastNode = node; // и последним становится созданный узел с задачкой, которая была в аргументе на входе
        }
    }

    /**
     * Метод удаления узла
     */
    private void removeNode(Node node) {
        if (node == null) return;
        Node prevNode = node.prev;
        Node nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        }
        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            lastNode = prevNode;
        }

        node.next = null;
        node.prev = null;

    }

    public boolean removeTask(Task task) {
        if (task == null) return false;
        Integer idTask = task.getId();
        Node nodeTask = mapNode.get(idTask);
        removeNode(nodeTask);  // удаляем узел из цепочки
        mapNode.remove(idTask); // удаляем из мапы значение (узел) по ключу - id задачи
        return true;
    }

    /**
     * Метод вывода списка задач из узлов цепочки
     *
     * @return список типа ArrayList<>()
     **/
    public List<Task> listNode() {
        List<Task> listHistory = new ArrayList<>();
        if (lastNode == null) {
            return listHistory;
        } else {
            Node node = lastNode;
            while (node != null) {
                listHistory.add(node.task);
                node = node.prev;
            }
        }
        return listHistory;
    }

}
