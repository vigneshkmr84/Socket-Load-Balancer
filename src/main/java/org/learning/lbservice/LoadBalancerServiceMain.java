package org.learning.lbservice;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;

public class LoadBalancerServiceMain {
    private static final ConcurrentMap<String, Boolean> concurrentHashMap = new ConcurrentHashMap<>();

    private static final Integer discoveryPort = 8082;

    public static void main(String[] args) {

        Comparator<Node> roundRobbinComparator = new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                if (n1.getWeight() > n2.getWeight()){
                    return 1;
                }else if (n1.getWeight()< n2.getWeight()){
                    return -1;
                }
                return 0;
            }
        };

        PriorityBlockingQueue<Node> queue = new PriorityBlockingQueue<>(2, roundRobbinComparator);

        HealthCheck healthCheck = new HealthCheck();

        Thread discoveryThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(discoveryPort)) {
                System.out.println("Discovery Server listening in : " + discoveryPort);
                while (true) {
                    Socket clientSocket = serverSocket.accept();

                    byte[] buffer = new byte[1024];
                    InputStream isStream = clientSocket.getInputStream();
                    String data = new String(buffer, 0, isStream.read(buffer));
                    int port = Integer.parseInt(data);

                    String hostName = clientSocket.getInetAddress().getHostName();
                    System.out.println("Hostname : " + hostName + ", Port : " + port);

                    String fullHostName = hostName + ":" + port;

                    // if hostname not there, make an entry
                    // else if hostname is false reset the request count
                    if (!concurrentHashMap.containsKey(hostName)) {
                        concurrentHashMap.put(fullHostName, true);
                        queue.put(new Node(fullHostName, 1, 0));
                    }else if (!concurrentHashMap.get(fullHostName)){
                        concurrentHashMap.put(fullHostName, true);
                        queue.put(new Node(fullHostName, 1, 0));
                    }

                    clientSocket.close();
                }
            } catch (Exception ex) {
                System.out.println("Exception occurred in listening : " + ex.getMessage());
            }
        });

        discoveryThread.start();

        Thread printerThread = new Thread(() -> {
            while (true) {
                System.out.println("Node Status : " + concurrentHashMap);
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // Print the status of the nodes for every 30 seconds
        printerThread.start();


        Thread healthCheckThread = new Thread(() -> {
            while (true) {
                try {

                    for (String hostName : concurrentHashMap.keySet()) {
                        // update the node status if it's not alive
                        if (!healthCheck.isHealthy(hostName)) {
                            System.out.println("Error: Node " + hostName + " is inactive.");
                            concurrentHashMap.put(hostName, false);
                        }else{
                            // previously inactive and came back online
                            concurrentHashMap.put(hostName, true);
                        }
                    }

                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.println("Exception occurred during health check : " + e.getMessage());
                }
            }
        });

        healthCheckThread.start();
    }
}
