package us.blint.securechat.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import us.blint.securechat.ui.ChatInterface;
import us.blint.securechat.ui.packet.display.DisplayConnectionAcceptedPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionDeclinedPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionRequestPacket;
import us.blint.securechat.ui.packet.display.DisplayMessagePacket;

/**
 *  <pre>
 *  Defines a connection
 *
 *  When this thread starts, it will prompt the user to accept the connection
 *  if the user did not initiate it.
 *      Case: User initiates request
 *          The thread will continuously probe for input from the socket 
 *              associated with this connection and print the input to the user. 
 *          When the BufferedReader returns null, the socket is closed.
 *      
 *      Case: Other user initiates request:
 *          Request the user to accept a connection. The thread will be blocked
 *              until the user either confirms or denies the request.
 *          If the connection is accepted, the thread will continuously probe for 
 *              input from the socket associated with this connection and print
 *              the input to the user. When the BufferedReader returns null, 
 *              the socket is closed.
 *          If the user does not accept the connection, the socket is closed.
 *  </pre>
 */
public class Connection extends Thread {
    private Socket s;
    private int connectionID;
    private ConnectionManager cm;
    private ChatInterface ui;
    private boolean accepted;
    private String ip;
    private int port;
    private boolean finished;
    private BufferedReader in;
    private PrintWriter out;
    
    /**
     *  Initialize variables
     *  Starts main thread
     * 
     *  @param s                   Socket between the client and another user
     *  @param connectionID        ID of this connection
     *  @param connectionManager   Manages all connections
     *  @param accepted            True if the user initiated the connection
     */
    public Connection(Socket s, int connectionID, ConnectionManager cm, ChatInterface ui, boolean accepted) {
        this.s = s;
        this.connectionID = connectionID;
        this.cm = cm;
        this.ui = ui;
        this.accepted = accepted;
        this.ip = s.getInetAddress().getHostName();
        this.port = s.getPort();
        finished = false;
        
        try {
            this.in = new BufferedReader(
                          new InputStreamReader(s.getInputStream()));
            this.out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) { e.printStackTrace(); }
        start();
    }
    
    public void run() {
        if(!accepted) {
            ui.send(new DisplayConnectionRequestPacket(connectionID, ip, port));
            boolean allowed = cm.requestConnection(connectionID);
            if(!allowed) {
                ui.send(new DisplayConnectionDeclinedPacket(connectionID));
                try {
                    s.close();
                } catch (IOException e) { e.printStackTrace(); }
            }   
            else {
                ui.send(new DisplayConnectionAcceptedPacket(connectionID, ip, port));
                // send 512 bit ECC public key
                // receive encrypted 256 bit key for AES
                // good to go
            }
        }
        else {
        	// listen for 512 bit ECC public key
        	ui.send(new DisplayConnectionAcceptedPacket(connectionID, ip, port));
        	// generate 256 bit key
        	// encrypt 256 bit key with ECC key
        	// send 256 bit key
        	// good to go
        }
        try {
            String line;
            while((line = in.readLine()) != null && !finished) {
                ui.send(new DisplayMessagePacket(line, connectionID));
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
     *  Returns the ID of this connection
     * 
     *  @return connectionID
     */
    public int getConnectionID() {
        return connectionID;
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
