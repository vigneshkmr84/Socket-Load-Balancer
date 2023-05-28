package org.learning.lbservice.temp;


import java.util.concurrent.LinkedBlockingDeque;


public class Test {
    public static void main(String[] args) {
        LinkedBlockingDeque<String> queue = new LinkedBlockingDeque<>();
        queue.add("Apple");
        queue.add("Banana");
        queue.add("Orange");

        // Peek at the first element
        String firstElement = queue.peek();

        System.out.println("First element: " + firstElement);
        System.out.println("Queue size: " + queue.size());
        queue.poll();
        queue.addFirst("first element");
        System.out.println("Queue size: " + queue.size());
        System.out.println(queue);
    }
}