package org.learning.lbservice;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class DiscoveryThread implements Runnable {

    ConcurrentHashMap<String, Boolean> concurrentHashMap;

    PriorityBlockingQueue<Node> queue;

    Integer discoveryPort;

    public DiscoveryThread(ConcurrentHashMap<String, Boolean> concurrentHashMap
            , PriorityBlockingQueue<Node> queue
            , Integer discoveryPort) {
        this.concurrentHashMap = concurrentHashMap;
        this.queue = queue;
        this.discoveryPort = discoveryPort;
    }

    @Override
    public void run() {

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
                } else if (!concurrentHashMap.get(fullHostName)) {
                    concurrentHashMap.put(fullHostName, true);
                    queue.put(new Node(fullHostName, 1, 0));
                }

                clientSocket.close();
            }
        } catch (Exception ex) {
            System.out.println("Exception occurred in listening : " + ex.getMessage());
        }
    }
}
