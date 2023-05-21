package org.learning.lbservice;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class ServerThread {
    ConcurrentHashMap<String, Boolean> concurrentHashMap;
    PriorityBlockingQueue<Node> queue;

    public ServerThread(ConcurrentHashMap<String, Boolean> concurrentHashMap
            , PriorityBlockingQueue<Node> queue) {

        this.concurrentHashMap = concurrentHashMap;
        this.queue = queue;
    }

    //    @Override
    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(9000)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();

                // new thread for each incoming connection
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }

        } catch (Exception ex) {
            System.out.println("Exception occurred " + ex.getMessage());
        }
    }

}

class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Read the client request
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder requestBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                System.out.println(line);
                requestBuilder.append(line).append("\r\n");
            }

            String request = requestBuilder.toString();

            // Handle null or empty request
            if (request.isEmpty()) {
                System.out.println("Empty Request");
                sendResponse("HTTP/1.1 400 Bad Request\r\n\r\n", clientSocket);
                clientSocket.close();
                return;
            }

            String[] requestParts = request.split(" ");

            if (requestParts.length < 2) {
                System.out.println("Invalid no of parameters");
                sendResponse("HTTP/1.1 400 Bad Request\r\n\r\n", clientSocket);
                clientSocket.close();
                return;
            }

            // Extract the HTTP method and URI
            String httpMethod = requestParts[0];
            String uri = requestParts[1];

            System.out.println("Method : " + httpMethod);
            System.out.println("uri : " + uri);

            // Process the request and generate the response
            String response = handleRequest(httpMethod, uri);
            sendResponse(response, clientSocket);

            // Close the client connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String handleRequest(String httpMethod, String uri) {
        // Handle different HTTP methods and URIs
        if (httpMethod.equals("GET")) {
            return "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: 12\r\n" +
                    "\r\n" +
                    "Hello, World!";
        } else {
            return "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: 9\r\n" +
                    "\r\n" +
                    "Not Found";
        }
    }

    private void sendResponse(String response, Socket clientSocket) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(response.getBytes());
        outputStream.flush();
    }
}
