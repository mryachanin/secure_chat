package us.blint.securechat.connection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import us.blint.securechat.ui.ChatInterface;
import us.blint.securechat.ui.packet.display.DisplayConnectionAcceptedPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionDeclinedPacket;
import us.blint.securechat.ui.packet.display.DisplayMessagePacket;

/**
 *  Defines a connection
 *
 *  When this thread starts, it will prompt the user to accept the connection.
 *      If the connection is accepted, the thread will continuously probe for 
 *      input from the socket associated with this connection and print the 
 *      input to the user. When the BufferedReader returns null, the socket is 
 *      closed
 *      If the user does not accept the connection, the socket is closed. 
 */
public class Connection extends Thread {
    private Socket s;
    private BufferedReader in;
    private PrintWriter out;
    private String connectionName;
    private ConnectionManager cm;
    private ChatInterface ui;
    
    /**
     *  Initializes variables
     *  Starts main thread
     * 
     *  @param s      Socket between the client and another user
     *  @param name   Name of this connection
     *  @param sysout Output stream to user
     *  @param ui     Defines the user interface
     */
    public Connection(Socket s, String connectionName, ConnectionManager cm, ChatInterface ui) {
        this.s = s;
        this.connectionName = connectionName;
        this.cm = cm;
        this.ui = ui;
        try {
            this.in = new BufferedReader(
                          new InputStreamReader(s.getInputStream()));
            this.out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) { e.printStackTrace(); }
        start();
    }
    
    public void run() {
        boolean allowed = cm.requestConnection(connectionName);
        if(!allowed) {
            ui.send(new DisplayConnectionDeclinedPacket(connectionName));
            try {
                s.close();
            } catch (IOException e) { e.printStackTrace(); }
        } else {
            ui.send(new DisplayConnectionAcceptedPacket(connectionName));
            try {
                String line;
                while((line = in.readLine()) != null) {
                    ui.send(new DisplayMessagePacket(line, connectionName));
                }
                s.close();
            } catch(IOException e) { e.printStackTrace(); }
        }
    }
    
    /**
     *  Sends a message to the socket's output stream
     * 
     *  @param message String to send over the socket
     */
    public void sendMessage(String message) {
        out.println(message);
    }
    
    /**
     *  Returns the name of this connection
     * 
     *  @return Name of this connection
     */
    public String getConnectionName() {
        return connectionName;
    }
    
    /**
     *  Returns if the socket is closed
     * 
     *  @return Boolean representing if the socket is closed
     */
    public boolean isClosed() {
        return s.isClosed();
    }
}
