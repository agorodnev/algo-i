import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private class Node<Item> {
        private Node<Item> next;
        private Node<Item> back;
        private Item item;
    }

    private Node<Item> head;
    private Node<Item> tail;
    private int size = 0;
    private final Node<Item> root;

    public Deque() {
        root = new Node<Item>();
        root.next = root;
        root.back = root;
        head = root;
        tail = root;
    }

    private void checkElement(Item item) { if (item == null) throw new NullPointerException(); }
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
    public void addFirst(Item item)
    {
        checkElement(item);
        Node<Item> next = new Node<Item>();
        next.item = item;
        if (head == root) {
            next.next = root;
            next.back = root;
            root.next = next;
            root.back = next;
            tail = next;
            head = next;
        } else {
            next.back = root;
            next.next = head;
            head.back = next;
            head = next;
            root.next = next;
            root.back = tail;
            tail.next = root;
        }
        size++;
    }
    public void addLast(Item item)
    {
        checkElement(item);
        Node<Item> next = new Node<Item>();
        next.item = item;
        if (tail == root) {
            next.next = root;
            next.back = root;
            root.next = next;
            root.back = next;
            tail = next;
            head = next;
        } else {
            next.next = root;
            next.back = tail;
            tail.next = next;
            tail = next;
            root.back = tail;
            root.next = head;
            head.back = root;
        }
        size++;
    }
    public Item removeFirst()
    {
        if (size == 0)
        {
            throw new java.util.NoSuchElementException();
        }
        Item item = head.item;
        if (head.next == root)
        {
            head = root;
            tail = root;
            root.next = root;
            root.back = root;
        } else {
            root.next = head.next;
            head = head.next;
            head.back = root;
        }
        size--;
        return item;
    }
    public Item removeLast()
    {
        if (size == 0)
        {
            throw new java.util.NoSuchElementException();
        }
        Item item = tail.item;
        if (tail.back == root)
        {
            head = root;
            tail = root;
            root.next = root;
            root.back = root;
        } else {
            root.back = tail.back;
            tail = tail.back;
            tail.next = root;
        }
        size--;
        return item;
    }

    public Iterator<Item> iterator() { return new DequeIterator(); }

    private class DequeIterator implements Iterator<Item>
    {
        private Node<Item> current = head;

        public boolean hasNext() { return current != root; }
        public void remove() { throw new UnsupportedOperationException(); }
        public Item next()
        {
            if (!hasNext()) { throw new java.util.NoSuchElementException(); }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args)
    {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("0");
        deque.addFirst("2");
        Iterator<String> it = deque.iterator();
        while(it.hasNext())
            StdOut.println(it.next());
    }
}
