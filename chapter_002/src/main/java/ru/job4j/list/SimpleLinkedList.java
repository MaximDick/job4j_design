package ru.job4j.list;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleLinkedList<E> implements SimpleList<E> {

    private int size;
    private Node<E> first;
    private Node<E> last;
    private int modCount;

    /**
     * Внутренний класс описывающий объекты Node, которые будут
     * храниться в главном хранилище и обеспечивать работу
     * двунаправленного связного списка.
     * У объектов есть поля.
     * date - хранимый в объекте элемент;
     * next - ссылка на следующий Node;
     * prev - ссылка на предыдущий Node.
     * @param <E> Параметризованный тип Node.
     * */
    public static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        /**
         * Конструктор инициализирует поля.
         * @param prev - ссылка на предыдущий элемент.
         * @param element - элемент хранящийся в Node.
         * @param next - ссылка на следующий элемент.
         *
         */
        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * @return Размер списка.
     * */
    public int getSize() {
        return size;
    }

    /**
     * Добавление элемента.
     * @param value объект, который необходимо добавить в хранилище.*/
    @Override
    public void add(E value) {
        this.modCount++;
        Node<E> newNode = new Node<>(this.last, value, null);
        if (size == 0) {
            this.first = newNode;
        } else {
            this.last.next = newNode;
        }
        this.last = newNode;
        size++;
    }


    /**
     * Возвращает ссылку на объект из хранилища по заданному индексу.
     * @param index заданный индекс.
     * @return ссылка на запрашиваемый объект.
     * */
    @Override
    public E get(int index) {
        return findNodeByIndex(index).item;
    }

    /**
     * Поиск узла по индексу.
     * @param index - индекс узла.
     * @return искомый узел.
     * */
    private Node<E> findNodeByIndex(int index) {
        int halfLinkedList = this.size / 2;
        Node<E> currentNode = this.first;
        if (index <= halfLinkedList) {
            for (int nodeCount = 0; nodeCount < index; nodeCount++) {
                currentNode = currentNode.next;
            }
        } else {
            currentNode = this.last;
            for (int nodeCount = size - 1; nodeCount > index; nodeCount--) {
                currentNode = currentNode.prev;
            }
        }
        return currentNode;
    }

    /**
     * @return - возвращает итератор для обхода списка.*/
    @Override
    public Iterator iterator() {
        return new Iterator() {
            private int nodeCount;
            private final int expectedmodCount = modCount;
            private Node<E> currentNode = first;

            /**
             * Проверяет наличие объектов за указателем.
             * @return true - если объект есть, false - если объектов нет.
             * */
            @Override
            public boolean hasNext() {
                return this.nodeCount < size;
            }

            /**
             * Возвращает текущий объект и переводит указатель.
             * @return ссылку на текузий объект под указателем.*/
            @Override
            public E next() {
                if (this.expectedmodCount != modCount) {
                    throw new ConcurrentModificationException();
                }
                if (this.nodeCount >= size) {
                    throw new NoSuchElementException();
                }
                E result = this.currentNode.item;
                this.currentNode = this.currentNode.next;
                this.nodeCount++;
                return result;
            }
        };
    }
}