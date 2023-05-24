package org.learning.lbservice;

import org.learning.lbservice.lb_types.LoadBalancer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerThread {
    ConcurrentHashMap<String, Boolean> concurrentHashMap;
//    PriorityBlockingQueue<Node> queue;

    LoadBalancer lb;


    public ServerThread(ConcurrentHashMap<String, Boolean> concurrentHashMap
            , LoadBalancer lb) {

        this.concurrentHashMap = concurrentHashMap;
//        this.queue = queue;
        this.lb = lb;
    }

    //    @Override
    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(9000)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();

                // new thread for each incoming connection
                Thread clientThread = new Thread(new ClientHandler(clientSocket, lb));
                clientThread.start();
            }

        } catch (Exception ex) {
            System.out.println("Exception occurred " + ex.getMessage());
        }
    }

}

class ClientHandler implements Runnable {

    private static Lock lock = new ReentrantLock();
    private final Socket clientSocket;

//    private PriorityBlockingQueue<Node> queue;

    LoadBalancer lb;

    public ClientHandler(Socket clientSocket
                         , LoadBalancer lb) {
        this.clientSocket = clientSocket;
        this.lb = lb;
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

            System.out.println("Method : " + httpMethod + ", URI : " + uri);

            // Process the request and generate the response
            String response = handleRequest(httpMethod, uri);
            sendResponse(response, clientSocket);

            // Close the client connection
//            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String handleRequest(String httpMethod, String uri) {
        // Handle different HTTP methods and URIs
        if (httpMethod.equals("GET")) {
            lock.lock();
            try {
                Node host = lb.getNextNode();
                System.out.println("Node chosen : " + host.getHostName());
                String response = restAPICall(host.getHostName(), uri);

                // update request count and put it back in the PQ
                Node updatedHost = new Node(host.getHostName(), host.getWeight(), host.getRequestCount() + 1);
                lb.insertNode(updatedHost);

                if (null != response) {

                    return "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: " + getContentLength(response) + "\r\n" +
                            "\r\n" +
                            response;
                }else{
                    System.out.println(host.getHostName() + " service returned a null response");
                }

            } catch (Exception e) {
                System.out.println("Exception occurred in handle request : " + e.getMessage());
            } finally {
                lock.unlock();
            }

        }
        return "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: 9\r\n" +
                "\r\n" +
                "Not Found";
    }

    private void sendResponse(String response, Socket clientSocket) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(response.getBytes());
        outputStream.flush();
    }

    private Integer getContentLength(String response) {
        return response.getBytes(StandardCharsets.UTF_8).length;
    }

    private String restAPICall(String hostName, String uri) {

        try {
            String u = "http://" + hostName + uri;
            System.out.println(u);
            URL finalURL = new URL(u);

            HttpURLConnection connection = (HttpURLConnection) finalURL.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();

            // buffered reader for reading the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();
            return response.toString();

        } catch (IOException e) {
            System.out.println("Exception occurred in making REST API call : " + e.getMessage());
        }

        return null;
    }
}
