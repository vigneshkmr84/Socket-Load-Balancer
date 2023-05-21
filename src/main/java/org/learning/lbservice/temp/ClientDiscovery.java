package org.learning.lbservice.temp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientDiscovery {

    static final int discovery_port = 8081;

    public void discovery() {
        try (ServerSocket serverSocket = new ServerSocket(discovery_port)) {
            System.out.println("Server listening on port " + discovery_port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Handle client connection in a separate thread
                Thread clientThread = new Thread(() -> {
                    try {
                        // Get the input/output streams from the socket
                        InputStream inputStream = clientSocket.getInputStream();
                        OutputStream outputStream = clientSocket.getOutputStream();

                        // Read data from the client
                        byte[] buffer = new byte[1024];
                        int bytesRead = inputStream.read(buffer);
                        String clientData = new String(buffer, 0, bytesRead);
                        System.out.println("Received from client: " + clientData);

                        // Process the client data (e.g., perform calculations, query a database)

                        // Send a response back to the client
                        String serverResponse = "Ok!";
                        outputStream.write(serverResponse.getBytes());

                        // Close the socket
                        clientSocket.close();
                        System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                // Start the client thread
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
