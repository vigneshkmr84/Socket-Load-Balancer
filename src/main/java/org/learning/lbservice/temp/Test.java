package org.learning.lbservice.temp;

import java.util.concurrent.BlockingQueue;

public class Test {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new java.util.concurrent.ArrayBlockingQueue<>(10);

        // Adding elements to the queue
        queue.add("Element 1");
        queue.add("Element 2");
        queue.add("Element 3");

        // Print elements in the queue
        for (String element : queue) {
            System.out.println(element);
        }

        System.out.println(queue.size());
    }
}