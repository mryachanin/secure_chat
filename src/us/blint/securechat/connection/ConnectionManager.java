package us.blint.securechat.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import us.blint.securechat.ui.ChatInterface;
import us.blint.securechat.ui.packet.display.DisplayConnectionRequestedPacket;
import us.blint.securechat.ui.packet.error.ConnectionRefusedErrorPacket;
import us.blint.securechat.ui.packet.error.DisconnectErrorPacket;
import us.blint.securechat.ui.packet.error.UnknownHostErrorPacket;

/**
 *  <pre>
 *  Provides an interface between the user and Connection objects
 *  
 *  This is a singleton class. Only 1 instance of it will ever be in existence.
 *  </pre>
 */
public class ConnectionManager {
    
    private static ConnectionManager instance = null;
    public static ConnectionManager getInstance() {
        if(instance == null)
            instance = new ConnectionManager();
        return instance;
    }
    
    private ChatInterface ui;
    private ArrayList<ConnectionRequest> pendingConnectionRequests;
    private HashMap<Integer, Connection> connectionMap;
    private int uniqueConnectionID;
    
    private ConnectionManager() {
        pendingConnectionRequests = new ArrayList<ConnectionRequest>();
        connectionMap = new HashMap<Integer, Connection>();
        uniqueConnectionID = 0;
    }

    /**
     *  Sets the user interface to interact with
     *  
     *  @param ui   User interface that extends ChatInterface
     */
    public void setUI(ChatInterface ui) {
        this.ui = ui;
    }
    
    /**
     *  Request the user to accept a connection. This method should block the
     *  calling thread until the user either confirms or denies the request.
     *
     *  @param connectionID   ID of the requested connection
     *
     *  @return true if the user accepts the connection, false otherwise
     */
    public boolean requestConnection(int connectionID) {
        ConnectionRequest request = new ConnectionRequest(connectionID);
        synchronized(pendingConnectionRequests) {
            pendingConnectionRequests.add(request);
        }
        try {
            synchronized(request) {
                request.wait();
            }
        } catch (InterruptedException e) { e.printStackTrace(); }
        
        return request.isAccepted();
    }
    
    /**
     *  Allows messages to be received through the socket associated with this ID
     * 
     *  @param connectionID   ID of the connection to accept
     */
    public void acceptConnection(int connectionID) {    
        synchronized(pendingConnectionRequests) {
            for(ConnectionRequest cr: pendingConnectionRequests) {
                if(cr.getConnectionID() == connectionID) {
                    cr.setAccepted(true);
                    synchronized(cr) {
                        cr.notify();
                    }
                    pendingConnectionRequests.remove(cr);
                    break;
                }
            }
        }
    }
    
    /**
     *  Does not allow traffic to be received through the socket associated 
     *  with this ID
     * 
     *  @param connectionID   ID of the connection to decline
     */
    public void declineConnection(int connectionID) {
        synchronized(pendingConnectionRequests) {
            for(ConnectionRequest cr: pendingConnectionRequests) {
                if(cr.getConnectionID() == connectionID) {
                    synchronized(cr) {
                        cr.notify();
                    }
                    pendingConnectionRequests.remove(cr);
                    break;
                }
            }
        }
    }
    
    /**
     *  <ul>
     *  <li>Instantiates a new connection that was requested by the Client</li>
     *  <li>Adds the connection to hashMap with key:value = connection_name : Connection</li>
     *  </ul>
     *  
     *  @param ip     IP Address of the socket for a new connection
     *  @param port   Port of the socket for a new connection
     */
    public void connect(String ip, int port) {
        int connectionID = uniqueConnectionID++;
        try {
            Socket s = new Socket(ip, port);
            Connection newConnection = new Connection(s, connectionID, this, ui, true);
            connectionMap.put(connectionID, newConnection);
            ui.send(new DisplayConnectionRequestedPacket(connectionID, ip, port));
        } catch (UnknownHostException e) {
            ui.send(new UnknownHostErrorPacket(e, connectionID));
        } catch(IOException e) {
            ui.send(new ConnectionRefusedErrorPacket(e, connectionID));
        }
    }

    /**
     *  Instantiates a new connection that was requested by a different Client
     *  Associates the connection with a name
     *  Adds the connection to hashMap with key:value = connection_name : Connection
     * 
     *  @param s   Socket for a new connection
     */
    public void connect(Socket s) {
        int connectionID = uniqueConnectionID++;
        Connection newConnection = new Connection(s, connectionID, this, ui, false);
        connectionMap.put(connectionID, newConnection);
    }
    
    /**
     *  Disconnect a current connection
     *  
     *  @param connectionID   ID of the connection to disconnect from
     */
    public void disconnect(int connectionID) {
        try {
            connectionMap.get(connectionID).close();
            connectionMap.remove(connectionID);
        } catch(Exception e) {
            ui.send(new DisconnectErrorPacket(e));
        }
    }
    
    /**
     *  Sends a message to a connection
     * 
     *  @param connectionID   ID of the connection to send messages
     *  @param message        Message to send to a connection
     */
    public void sendMessage(int connectionID, String message) {
        connectionMap.get(connectionID).sendMessage(message);
    }
}
