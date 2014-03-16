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
 *  When this thread starts, it will prompt the user to accept the connection.
 *      If the connection is accepted, the thread will continuously probe for 
 *      input from the socket associated with this connection and print the 
 *      input to the user. When the BufferedReader returns null, the socket is 
 *      closed
 *      If the user does not accept the connection, the socket is closed. 
 */
public class Connection extends Thread {
    private String id;
    private Socket s;
    private BufferedReader in;
    private PrintWriter out;
    private String connectionName;
    private int connectionNumber;
    private ConnectionManager cm;
    private ChatInterface ui;
    private boolean finished;
    
    /**
     *  Initializes variables
     *  Starts main thread
     * 
     *  @param s    Socket between the client and another user
     *  @param connectionName       Name of this connection
     *  @param connectionNumber     Unique number of this connection
     *  @param connectionManager    Manages all connections
     */
    public Connection(Socket s, String connectionName, int connectionNumber, ConnectionManager cm) {
        this.id = new String(id);
        this.s = s;
        this.connectionName = connectionName;
        this.connectionNumber = connectionNumber;
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
        boolean allowed = cm.requestConnection(connectionName, connectionNumber);
        if(!allowed) {
            ui.send(new DisplayConnectionDeclinedPacket(connectionName));
            try {
                s.close();
            } catch (IOException e) { e.printStackTrace(); }
        } else {
            ui.send(new DisplayConnectionAcceptedPacket(connectionName));
            try {
                String line;
                while((line = in.readLine()) != null && !finished) {
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
     *  Returns the unique id of this connection
     *  
     *  @return Unique id of this connection
     */
    public int getConnectionNumber() {
        return connectionNumber;
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
    
    /**
     *  Returns if the socket is closed
     * 
     *  @return Boolean representing if the socket is closed
     */
    public boolean isClosed() {
        return s.isClosed();
    }
}
