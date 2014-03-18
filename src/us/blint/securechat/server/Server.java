package us.blint.securechat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import us.blint.securechat.client.Client;
import us.blint.securechat.connection.ConnectionManager;
import us.blint.securechat.ui.ChatInterface;
import us.blint.securechat.ui.packet.display.DisplayConnectionRequestPacket;
import us.blint.securechat.ui.packet.display.DisplayServerStartPacket;

/**
 *  <pre>
 *  Defines a server that will constantly listen for connection requests
 *
 *  When started as a thread, this will:
 *      Continuously listen for new connection requests.
 *      When a new connection is found, the server will create a new 
 *          Connection object and send a packet to the user interface to signify
 *          that a connection is pending.
 *          All validation is handled in the Connection object so multiple 
 *          requests can be pending at once.
 *  </pre>
 */
public class Server extends Thread {
    private ServerSocket serverSocket;
    private int serverPort = 0;
    ConnectionManager cm;
    ChatInterface ui;
    
    /**
     *  Instantiates a new server socket
     *  Sends user interface a packet with the port this server started on
     */
    public Server(ChatInterface ui) {
        cm = ConnectionManager.getInstance();
        this.ui = ui;
        try {
            serverSocket = new ServerSocket(serverPort);
            ui.send(new DisplayServerStartPacket(serverSocket.getLocalPort()));
        } catch (IOException e) { e.printStackTrace(); }     
    }
    
    @Override
    public void run() {
        Socket clientSocket;
        try {
            clientSocket = serverSocket.accept();
            String ip = clientSocket.getInetAddress().getHostName();
            ui.send(new DisplayConnectionRequestPacket(ip, clientSocket.getPort()));
            cm.connect(clientSocket, ip);
        } catch (IOException e) { e.printStackTrace(); }
    }
}