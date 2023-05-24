package org.learning.lbservice;

import org.learning.lbservice.lb_types.LoadBalancer;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class DiscoveryServerThread implements Runnable {

    ConcurrentHashMap<String, Boolean> concurrentHashMap;

//    PriorityBlockingQueue<Node> queue;

    LoadBalancer lb;

    Integer discoveryPort;

    public DiscoveryServerThread(ConcurrentHashMap<String, Boolean> concurrentHashMap
            , LoadBalancer lb
            , Integer discoveryPort) {
        this.concurrentHashMap = concurrentHashMap;
//        this.queue = (PriorityBlockingQueue<Node>) lb.getQueue();
        this.lb = lb;
        this.discoveryPort = discoveryPort;
    }

    @Override
    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(discoveryPort)) {
            System.out.printf("Discovery Server listening in : %d\n", discoveryPort);
            while (true) {
                Socket clientSocket = serverSocket.accept();

                byte[] buffer = new byte[1024];
                InputStream isStream = clientSocket.getInputStream();
                String data = new String(buffer, 0, isStream.read(buffer));
                int port = Integer.parseInt(data);

                String hostName = clientSocket.getInetAddress().getHostName();
                String fullHostName = hostName + ":" + port;

                // if hostname not there, make an entry
                if (!concurrentHashMap.containsKey(fullHostName)) {
                    concurrentHashMap.put(fullHostName, true);
                    lb.insertNode(new Node(fullHostName, 1, 0));
                    System.out.printf("New Hostname Registered : %s, port : %s \n", hostName, port);
                }
                // else if hostname status is false reset the request count
                else if (!concurrentHashMap.get(fullHostName)) {
                    concurrentHashMap.put(fullHostName, true);
                    lb.insertNode(new Node(fullHostName, 1, 0));
                }

                clientSocket.close();
            }
        } catch (Exception ex) {
            System.out.println("Exception occurred in listening : \n" + ex.getMessage());
        }
    }
}
