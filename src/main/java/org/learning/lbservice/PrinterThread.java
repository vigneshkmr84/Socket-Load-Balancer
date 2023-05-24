package org.learning.lbservice;

import org.learning.lbservice.lb_types.LoadBalancer;

import java.util.concurrent.ConcurrentHashMap;

public class PrinterThread implements Runnable {

    ConcurrentHashMap<String, Boolean> concurrentHashMap;
    LoadBalancer lb;

    public PrinterThread(ConcurrentHashMap<String, Boolean> concurrentHashMap
            , LoadBalancer lb) {
        this.concurrentHashMap = concurrentHashMap;
        this.lb = lb;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Node Status : " + concurrentHashMap);
            lb.print();
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
