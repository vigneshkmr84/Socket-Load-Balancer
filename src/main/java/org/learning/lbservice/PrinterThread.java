package org.learning.lbservice;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class PrinterThread implements Runnable {

    ConcurrentHashMap<String, Boolean> concurrentHashMap;
    PriorityBlockingQueue<Node> queue;

    public PrinterThread(ConcurrentHashMap<String, Boolean> concurrentHashMap
            , PriorityBlockingQueue<Node> queue) {
        this.concurrentHashMap = concurrentHashMap;
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Node Status : " + concurrentHashMap);
            System.out.println(queue);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
