import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] d;
    private int size = 0;
    private int n = 4;
    public RandomizedQueue() { d = (Item[]) new Object[n]; }
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
    public void enqueue(Item item)
    {
        if (item == null) { throw new java.lang.NullPointerException(); }
        if (size == n - 1)
        {
            n *= 2;
            Item[] tmp = (Item[]) new Object[n];
            for (int i = 0; i < size; i++)
            {
                tmp[i] = d[i];
                d[i] = null;
            }
            d = tmp;
        }
        d[size++] = item;
    }
    public Item dequeue()
    {
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }
        int idx = StdRandom.uniform(size);
        Item el = d[idx];
        d[idx] = d[size-1];
        d[size-1] = null;
        size--;
        if (size > 4 && size < n/4)
        {
            Item[] tmp = (Item[]) new Object[n/2];
            for (int i = 0; i < n/2; i++)
            {
                tmp[i] = d[i];
            }
            for (int i = 0; i < n; i++)
            {
                d[i] = null;
            }
            d = tmp;
            n /= 2;
        }
        return el;
    }        
    public Item sample()
    {
        if (isEmpty()) { throw new java.util.NoSuchElementException(); }
        int idx = StdRandom.uniform(size);
        return d[idx];
    }
    public Iterator<Item> iterator() { return new RandomizedQueueIterator(); }

    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private Item[] rq;
        private int idx;
        public RandomizedQueueIterator()
        {
            rq = (Item[]) new Object[size];
            idx = size;
            if (size > 0) {
                for (int i = 0; i < idx; i++)
                {
                    rq[i] = d[i];
                }
                StdRandom.shuffle(rq);
            }
        }
        public boolean hasNext() { return idx != 0; }
        public void remove() { throw new java.lang.UnsupportedOperationException(); }
        public Item next()
        {
            if (!hasNext()) { throw new java.util.NoSuchElementException(); }
            return rq[--idx];
        }
    }
}
