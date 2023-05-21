package org.learning.lbservice.temp;

import org.learning.lbservice.HealthCheck;

public class Main {
    public static void main(String[] args) {
        HealthCheck check = new HealthCheck();
        System.out.println(check.isHealthy("localhost:8080"));
    }
}
