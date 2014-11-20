package com.psychocoder.JStack;

/**
 *
 * @author Psycho_Coder
 * @param <T>
 */
public interface StackInterface<T> {

    /**
     *
     * @param item This method push the item passed into the stack
     */
    public abstract void push(T item);

    /**
     *
     * @return This method removes the top element from the stack
     */
    public abstract T pop();

    /**
     *
     * @return This method returns whether the stack is empty or not
     */
    public abstract boolean isEmpty();

    /**
     *
     * @return This Methods returns the top element of the stack
     */
    public abstract T peek();

    /**
     *
     * @return This method returns the size of the stack
     */
    public abstract int size();

    /**
     *
     * @return This method returns the array of the items in the stack
     */
    public abstract String display();

}
