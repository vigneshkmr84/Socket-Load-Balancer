package org.learning.lbservice.temp;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.add("Apple");
        queue.add("Banana");
        queue.add("Orange");

        // Peek at the first element
        String firstElement = queue.peek();

        System.out.println("First element: " + firstElement);
        System.out.println("Queue size: " + queue.size());
        queue.poll();
        System.out.println("Queue size: " + queue.size());
    }
}