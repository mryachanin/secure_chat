package us.blint.securechat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import us.blint.securechat.connection.ConnectionManager;
import us.blint.securechat.ui.ChatInterface;

/**
 *  Defines a server that will constantly listen for connection requests
 *
 *  When started as a thread, this will:
 *      Continuously listen for new connection requests.
 *      When a new connection is found, the server will create a new 
 *          Connection object. All validation is handled in the Connection 
 *          object so multiple requests can be pending at once.
 */
public class Server extends Thread {
    private ServerSocket serverSocket;
    private int serverPort = 0;
    ConnectionManager cm;
    ChatInterface ui;
    
    /**
     *  Instantiates a new server socket
     *  Prints the port it was started on
     */
    public Server() {
        cm = ConnectionManager.getInstance();
        try {
            serverSocket = new ServerSocket(serverPort);
            //this.out.println("Server started on: " + serverSocket.getLocalPort());
        } catch (IOException e) { e.printStackTrace(); }     
    }
    
    @Override
    public void run() {
        Socket clientSocket;
        try {
            clientSocket = serverSocket.accept();
            String ip = clientSocket.getInetAddress().toString();
            //out.println("#### Incoming Request From " + ip + " ####");
            cm.connect(clientSocket, ip);
        } catch (IOException e) { e.printStackTrace(); }
    }
}