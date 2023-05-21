package org.learning.lbservice;

import java.net.InetSocketAddress;
import java.net.Socket;

public class HealthCheck {

    private static final Integer TIMEOUT_SECONDS = 1000; // 1 sec

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

}
