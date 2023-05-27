package org.learning.lbservice;

import org.learning.lbservice.lb_types.LoadBalancer;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class DiscoveryServerThread implements Runnable {

    ConcurrentHashMap<String, Boolean> concurrentHashMap;

    LoadBalancer lb;

    Integer discoveryPort;

    public DiscoveryServerThread(ConcurrentHashMap<String, Boolean> concurrentHashMap
            , LoadBalancer lb
            , Integer discoveryPort) {
        this.concurrentHashMap = concurrentHashMap;
        this.lb = lb;
        this.discoveryPort = discoveryPort;
    }

    @Override
    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(discoveryPort)) {
            System.out.printf("Discovery Server listening in : %d\n", discoveryPort);
            while (true) {
                Socket clientSocket = serverSocket.accept();

                ObjectInputStream isStream = new ObjectInputStream(clientSocket.getInputStream());
                Object receivedObject = isStream.readObject();
                Map<String, Object> map = new HashMap<>();
                if (receivedObject instanceof Map)
                    map = (Map) receivedObject;

                int port = Integer.parseInt((String) map.get("hostPort"));
                int nodeWeight = (int) map.get("nodeWeight");

                String hostName = clientSocket.getInetAddress().getHostName();
                String fullHostName = hostName + ":" + port;
                int nodeId = (int) map.get("nodeId");
                // if hostname not there, make an entry
                if (!concurrentHashMap.containsKey(fullHostName)) {
                    concurrentHashMap.put(fullHostName, true);
                    lb.insertNode(new Node(fullHostName, nodeWeight, 0, nodeId));
                    System.out.printf("New Hostname Registered : %s, port : %s \n", hostName, port);
                }
                // else if hostname status is false reset the request count
                else if (!concurrentHashMap.get(fullHostName)) {
                    concurrentHashMap.put(fullHostName, true);
                    lb.insertNode(new Node(fullHostName, nodeWeight, 0, nodeId));
                }

                clientSocket.close();
            }
        } catch (Exception ex) {
            System.out.println("Exception occurred in listening : \n" + ex);
            ex.printStackTrace();
        }
    }
}
