package org.learning.lbservice;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HealthCheckThread implements Runnable {

    private static final Integer TIMEOUT_SECONDS = 1000; // 1 sec

    private ConcurrentHashMap<String, Boolean> concurrentHashMap;

    public HealthCheckThread(ConcurrentHashMap<String, Boolean> concurrentHashMap) {
        this.concurrentHashMap = concurrentHashMap;
    }

    public boolean isHealthy(String hostName) {

        try (Socket clientSocket = new Socket()) {
            clientSocket.connect(new InetSocketAddress(hostName.split(":")[0]
                    , Integer.parseInt(hostName.split(":")[1])), TIMEOUT_SECONDS);
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred during connection to Host : " + hostName);
            return false;
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                for (Map.Entry<String, Boolean> host : concurrentHashMap.entrySet()) {
                    concurrentHashMap.put(host.getKey(), isHealthy(host.getKey()));
                }
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Error occurred during health check");
            }
        }
    }
}
