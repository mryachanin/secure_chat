package us.blint.securechat.connection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import us.blint.securechat.client.Client;
import us.blint.securechat.ui.ChatInterface;
import us.blint.securechat.ui.packet.display.DisplayConnectionAcceptedPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionDeclinedPacket;
import us.blint.securechat.ui.packet.display.DisplayMessagePacket;

/**
 *  Defines a connection
 *
 *  When this thread starts, it will prompt the user to accept the connection 
 *  if the user did not initiate it.
 *      Case: User initiates request
 *          the thread will continuously probe for input from the socket 
 *          associated with this connection and print the input to the user. 
 *          When the BufferedReader returns null, the socket is closed.
 *      
 *      Case: Other user initiates request:
 *          If the connection is accepted, the thread will continuously probe for 
 *          input from the socket associated with this connection and print the 
 *          input to the user. When the BufferedReader returns null, the socket is 
 *          closed.
 *          If the user does not accept the connection, the socket is closed. 
 */
public class Connection extends Thread {
    private Socket s;
    private BufferedReader in;
    private PrintWriter out;
    private String connectionName;
    private ConnectionManager cm;
    private ChatInterface ui;
    private boolean accepted, finished;
    
    /**
     *  Initializes variables
     *  Starts main thread
     * 
     *  @param s                   Socket between the client and another user
     *  @param connectionName      Name of this connection
     *  @param connectionManager   Manages all connections
     *  @param accepted            True if the user initiated the connection
     */
    public Connection(Socket s, String connectionName, ConnectionManager cm, boolean accepted) {
        this.s = s;
        this.connectionName = connectionName;
        this.accepted = accepted;
        finished = false;
        
        this.cm = cm;
        this.ui = Client.getChatInterface();
        try {
            this.in = new BufferedReader(
                          new InputStreamReader(s.getInputStream()));
            this.out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) { e.printStackTrace(); }
        start();
    }
    
    public void run() {
        if(!accepted) {
            boolean allowed = cm.requestConnection(s.getInetAddress().getHostName(), s.getPort());
            if(!allowed) {
                ui.send(new DisplayConnectionDeclinedPacket(connectionName, s.getPort()));
                try {
                    s.close();
                } catch (IOException e) { e.printStackTrace(); }
            }   
            else 
                ui.send(new DisplayConnectionAcceptedPacket(connectionName));
        }
        try {
            String line;
            while((line = in.readLine()) != null && !finished) {
                ui.send(new DisplayMessagePacket(line, connectionName));
            }
            s.close();
        } catch(IOException e) { e.printStackTrace(); }
    }
    
    /**
     *  Sends a message to the socket's output stream
     * 
     *  @param message   String to send over the socket
     */
    public void sendMessage(String message) {
        out.println(message);
    }
    
    /**
     *  Returns the name of this connection
     * 
     *  @return connectionName
     */
    public String getConnectionName() {
        return connectionName;
    }
    
    /**
     *  Sets the name of this connection
     *  
     *  @param connectionName   New name of this connection
     */
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }
    
    /**
     *  Sets finished to true. This will exit the while loop in run() and 
     *  close the socket.
     *  
     *  @throws IOException
     */
    public void close() throws IOException {
        finished = true;
    }
}
