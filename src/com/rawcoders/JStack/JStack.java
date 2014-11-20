package com.rawcoders.JStack;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Psycho_Coder
 * @param <T>
 */
public class JStack<T> implements StackInterface<T> {

    private LinkedList<T> list;

    public JStack() {
        this.list = new LinkedList<>();
    }

    /* 
     * @see com.psychocoder.JStack.StackInterface#push(java.lang.Object)
     */
    @Override
    public synchronized void push(T item) {
        list.addFirst(item);
    }

    /* 
     * @see com.psychocoder.JStack.StackInterface#pop()
     */
    @Override
    public synchronized T pop() {
        return list.removeFirst();
    }

    /* 
     * @see com.psychocoder.JStack.StackInterface#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /* 
     * @see com.psychocoder.JStack.StackInterface#peek()
     */
    @Override
    public synchronized T peek() {
        return list.getFirst();
    }

    /* 
     * @see com.psychocoder.JStack.StackInterface#size()
     */
    @Override
    public int size() {
        return list.size();
    }

    /* 
     * @see com.psychocoder.JStack.StackInterface#display()
     */
    @Override
    public String display() {
        String m = "";
        for (Iterator<T> it = list.iterator(); it.hasNext();) {
            m = m + it.next() + " ";
        }
        return m;
    }

    /**
     * @returns An array of all the element in the stack
     */
    public Object[] elements() {
        return list.toArray();
    }
}
