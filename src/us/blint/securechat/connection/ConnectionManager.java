package us.blint.securechat.connection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import us.blint.securechat.client.Client;
import us.blint.securechat.ui.ChatInterface;
import us.blint.securechat.ui.packet.error.ConnectErrorPacket;
import us.blint.securechat.ui.packet.error.ConnectionRefusedErrorPacket;
import us.blint.securechat.ui.packet.error.DisconnectErrorPacket;

/**
 *  Provides an interface between the user and Connection objects
 *  
 *  This is a singleton class. Only 1 instance of it will ever be in existence.
 */
public class ConnectionManager {
    
    private static ConnectionManager instance = null;
    public static ConnectionManager getInstance() {
        if(instance == null)
            instance = new ConnectionManager();
        return instance;
    }
    
    private ChatInterface ui;
    private ArrayList<Connection> connections;
    private ArrayList<ConnectionRequest> pendingConnections;
    private HashMap<String,Connection> connectionMap;
    private int connectionNumber;
    
    private ConnectionManager() {
        this.ui = Client.getChatInterface();
        connections = new ArrayList<Connection>();
        connectionMap = new HashMap<String,Connection>();
        connectionNumber = 0;
    }

    /**
     *  Request the user to accept a connection. This method should block the
     *  calling thread until the user either confirms or denies the request.
     *
     *  @param connectionName The identifier for the connection
     *
     *  @return true is the user accepts the connection, false otherwise
     */
    public boolean requestConnection(String connectionName, int connectionNumber) {
        ConnectionRequest request = new ConnectionRequest(connectionName, connectionNumber);
        synchronized(pendingConnections) {
            pendingConnections.add(request);
        }
        try {
            synchronized(request) {
                request.wait();
            }
        } catch (InterruptedException e) { e.printStackTrace(); }
        
        return request.isAccepted();
    }
    
    /**
     *  Allows messages to be received through the socket associated with this
     *  Connection id
     * 
     *  @param connectionNumber Unique id of the request to accept
     */
    public void acceptConnection(int connectionNumber) {
        synchronized(pendingConnections) {
            for(ConnectionRequest cr: pendingConnections) {
                if(cr.getConnectionNumber() == connectionNumber) {
                    cr.setAccepted(true);
                    synchronized(cr) {
                        cr.notify();
                    }
                    pendingConnections.remove(cr);
                    break;
                }
            }
        }
    }
    
    /**
     *  Does not allow traffic to be received through the socket associated 
     *  with this Connection id
     * 
     *  @param connectionNumber Unique id of the request to decline
     */
    public void declineConnection(int connectionNumber) {
        synchronized(pendingConnections) {
            for(ConnectionRequest cr: pendingConnections) {
                if(cr.getConnectionNumber() == connectionNumber) {
                    synchronized(cr) {
                        cr.notify();
                    }
                    pendingConnections.remove(cr);
                    break;
                }
            }
        }
    }
    
    /**
     *  passes overloaded method a new socket and the connection name to
     *      instantiate a new connection
     *  
     *  @param ip IP Address of the socket for a new connection
     *  @param port Port of the socket for a new connection
     *  @param connectionName Name of the connection for ease of reference
     */
    public void connect(String ip, int port, String connectionName) {
        try {
            connect(new Socket(ip, port), connectionName);
        } catch (ConnectException e) {
            ui.send(new ConnectionRefusedErrorPacket(e, connectionName));
        } catch(IOException e) {
            ui.send(new ConnectErrorPacket(e));
        }
    }

    /**
     *  Instantiates a new connection and associates it with a connection name
     * 
     *  @param  s Socket for a new connection
     *  @param  connectionName Name of the connection for ease of reference
     */
    public void connect(Socket s, String connectionName) {
        Connection newConnection = new Connection(s, connectionName, getNextConnectionNumber(), this);
        connectionMap.put(connectionName, newConnection);
        connections.add(newConnection);
    }
    
    /**
     *  Disconnect a current connection
     *  
     *  @param connectionName Name of the connection to disconnect from
     */
    public void disconnect(String connectionName) {
        try {
            connectionMap.get(connectionName).close();
            connections.remove(connectionMap.get(connectionName));
        } catch(Exception e) {
            ui.send(new DisconnectErrorPacket(e));
        }
    }
    
    /**
     *  Continuously sends messages to a connection until user types '/close'
     * 
     *  @param connectionName Name of the connection to send messages
     */
    public void sendMessage(String connectionName, String message) {
        connectionMap.get(connectionName).sendMessage(message);
    }
    
    /**
     *  Provides a unique id and increments that id in preparation for the next call
     *  
     *  @return Unique id to associate with a connection
     */
    public int getNextConnectionNumber() {
        return connectionNumber++;
    }
}
