package org.learning.lbservice;

import org.learning.lbservice.lb_types.LBFactory;
import org.learning.lbservice.lb_types.LoadBalancer;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class LoadBalancerServiceMain {

    private static final ConcurrentHashMap<String, Boolean> concurrentHashMap = new ConcurrentHashMap<>();

//    private static final Integer discoveryPort = 8082;

    public static void main(String[] args) {

        Comparator<Node> roundRobbinComparator = (n1, n2) -> {
            if (n1.getRequestCount() > n2.getRequestCount()) {
                return 1;
            } else if (n1.getRequestCount() < n2.getRequestCount()) {
                return -1;
            }
            return 0;
        };

        Integer discoveryPort = Integer.parseInt(args[0]);
        String lbType = args[1].toUpperCase();

        LBFactory factory = new LBFactory();
        LoadBalancer loadBalancer = factory.getLB(lbType);

        System.out.println("Load Balancer Type : " + loadBalancer.getType());

        Thread discoveryThread = new Thread(new DiscoveryServerThread(concurrentHashMap, loadBalancer, discoveryPort));
        discoveryThread.start();

        Thread printerThread = new Thread(new PrinterThread(concurrentHashMap, loadBalancer));
        printerThread.start();

        Thread healthCheckThread = new Thread(new HealthCheckThread(concurrentHashMap));
        healthCheckThread.start();

        ServerThread serverThread = new ServerThread(concurrentHashMap, loadBalancer);
        serverThread.run();

    }
}
