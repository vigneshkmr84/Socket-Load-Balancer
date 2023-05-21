package org.learning.lbservice;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class LoadBalancerServiceMain {

    private static final ConcurrentHashMap<String, Boolean> concurrentHashMap = new ConcurrentHashMap<>();

    private static final Integer discoveryPort = 8082;

    public static void main(String[] args) {

        Comparator<Node> roundRobbinComparator = (n1, n2) -> {
            if (n1.getWeight() > n2.getWeight()) {
                return 1;
            } else if (n1.getWeight() < n2.getWeight()) {
                return -1;
            }
            return 0;
        };

        PriorityBlockingQueue<Node> queue = new PriorityBlockingQueue<>(2, roundRobbinComparator);

        Thread discoveryThread = new Thread(new DiscoveryThread(concurrentHashMap, queue, discoveryPort));
        discoveryThread.start();

        Thread printerThread = new Thread(new PrinterThread(concurrentHashMap));
        printerThread.start();

        Thread healthCheckThread = new Thread(new HealthCheck(concurrentHashMap));
        healthCheckThread.start();

    }
}
