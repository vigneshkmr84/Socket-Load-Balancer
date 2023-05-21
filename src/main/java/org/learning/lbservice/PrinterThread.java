package org.learning.lbservice;

import java.util.concurrent.ConcurrentHashMap;

public class PrinterThread implements Runnable {

    ConcurrentHashMap<String, Boolean> concurrentHashMap;

    public PrinterThread(ConcurrentHashMap<String, Boolean> concurrentHashMap) {
        this.concurrentHashMap = concurrentHashMap;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Node Status : " + concurrentHashMap);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
