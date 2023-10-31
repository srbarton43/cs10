/**
 *
 * @param <T> generic Object T
 *
 * @author Sam Barton 9/26/2021
 */
public class SinglyLinkedHT<T> implements SimpleList<T> {
    private int size;
    private Element head, tail;

    private class Element
    {
        private T value;
        private Element next;
        public Element(T val, Element nxt)
        {
            value = val;
            next = nxt;
        }
    }

    public SinglyLinkedHT()
    {
        head = tail = null;
        size = 0;
    }

    // traverses n elements in list
    private Element advance(int n) throws Exception {
        Element e = head;
        while(n>0)
        {
            e=e.next;
            if(e==null) throw new Exception("invalid index");
            n--;
        }
        return e;
    }
    /**
     * Returns # elements in the list (they are indexed 0..size-1)
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Adds the item at the index, which must be between 0 and size
     * (since the current elements are 0..size-1, idx = size grows the list)
     *
     * @param idx index
     * @param item item of abstract Object T
     */
    @Override
    public void add(int idx, T item) throws Exception
    {
        if(idx<0)
        {
            throw new Exception("invalid index");
        }
        else if (idx==0)
        {
            head = new Element(item, head);
            tail = head;
        }
        else if(idx==size)
        {
            tail.next = new Element(item, null);
            tail = tail.next;
        }
        else
        {
            Element e = advance(idx);
            e.next = new Element(item, e.next);
        }
        size++;
    }

    /**
     * Removes the item at the index, which must be between 0 and size-1
     *
     * @param idx index
     */
    @Override
    public void remove(int idx) throws Exception
    {
        if(idx<0)
        {
            throw new Exception("invalid index");
        }
        else if(idx==0)
        {
            if(head==null) throw new Exception("invalid index");
            head = head.next;
        }
        else if(idx==size-1)
        {
            tail = advance(size-2);
            tail.next = null;
        }
        else
        {
            Element e = advance(idx-1);
            if(e.next == null) throw new Exception("invalid index");
            e.next = e.next.next;
        }
        size--;
    }

    /**
     * Returns the item at the index, which must be between 0 and size-1
     *
     * @param idx index
     */
    @Override
    public T get(int idx) throws Exception
    {
        if(idx<0)
        {
            throw new Exception("invalid index");
        }
        else if (idx == 0)
        {
            return head.value;
        }
        else if (idx == size-1)
        {
            return tail.value;
        }
        else
        {
            Element e = advance(idx);
            if(e==null) throw new Exception("invalid index");
            return e.value;
        }
    }

    /**
     * Replaces the item at the index, which must be between 0 and size-1
     *
     * @param idx index
     * @param item item of abstract type T
     */
    @Override
    public void set(int idx, T item) throws Exception
    {
        if(idx<0) throw new Exception("invalid index");
        if(idx == size-1)
        {
            tail.value = item;
        }
        else
        {
            Element e = advance(idx);
            e.value = item;
        }
    }

    /**
     * appends parameter list to this list
     *
     * @param other other SinglyLinkedListHT to append to this one
     */
    public void append(SinglyLinkedHT<T> other)
    {
        if(other.size == 0) return;
        if(size == 0) {
            head = other.head;
            size = other.size;
        }

        else
        {
            tail.next = other.head;
            size = this.size + other.size;
        }
        tail = other.tail;
    }

    public String toString()
    {
        String string = "";
        for(Element x = head; x != null; x=x.next)
            string+=x.value + "->";
        string+="[/]";
        return string;
    }

    public static void main(String[] args) throws Exception {
        SinglyLinkedHT<String> list1 = new SinglyLinkedHT<String>();
        SinglyLinkedHT<String> list2 = new SinglyLinkedHT<String>();

        System.out.println(list1 + " + " + list2); // [/] + [/]
        list1.append(list2);
        System.out.println(" = " + list1); // = [/]

        list2.add(0, "a");
        System.out.println(list1 + " + " + list2); // [/] + a->[/]
        list1.append(list2);
        System.out.println(" = " + list1); // = a->[/]

        list1.add(1, "b");
        list1.add(2, "c");
        SinglyLinkedHT<String> list3 = new SinglyLinkedHT<String>();
        System.out.println(list1 + " + " + list3); // a->b->c->[/] + [/]
        list1.append(list3);
        System.out.println(" = " + list1); // = a->b->c->[/]

        SinglyLinkedHT<String> list4 = new SinglyLinkedHT<String>();
        list4.add(0, "z");
        list4.add(0, "y");
        list4.add(0, "x");
        System.out.println(list1 + " + " + list4); // a->b->c->[/] + x->y->z->[/]
        list1.append(list4);
        System.out.println(" = " + list1); // a->b->c->x->y->z->[/}

        list1.remove(5);
        list1.remove(4);
        SinglyLinkedHT<String> list5 = new SinglyLinkedHT<String>();
        list5.add(0, "z");
        list5.add(0, "y");
        System.out.println(list1 + " + " + list5); // a->b->c->x->[/] + y->z->[/]
        list1.append(list5);
        System.out.println(" = " + list1); // a->b->c->x->y->z->[/]
    }
}
