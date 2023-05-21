package org.learning.lbservice;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class HealthCheck implements Runnable {

    private static final Integer TIMEOUT_SECONDS = 1000; // 1 sec

    private ConcurrentHashMap<String, Boolean> concurrentHashMap;

    public HealthCheck(ConcurrentHashMap<String, Boolean> concurrentHashMap) {
        this.concurrentHashMap = concurrentHashMap;
    }

    public boolean isHealthy(String hostName) {

        try (Socket clientSocket = new Socket()) {
            clientSocket.connect(new InetSocketAddress(hostName.split(":")[0]
                    , Integer.parseInt(hostName.split(":")[1])), TIMEOUT_SECONDS);
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred during connection");
            return false;
        }
    }

    @Override
    public void run() {

    }
}
