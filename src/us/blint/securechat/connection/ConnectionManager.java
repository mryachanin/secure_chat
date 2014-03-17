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
    
    private ConnectionManager() {
        this.ui = Client.getChatInterface();
        connections = new ArrayList<Connection>();
        pendingConnections = new ArrayList<ConnectionRequest>();
        connectionMap = new HashMap<String,Connection>();
    }

    /**
     *  Request the user to accept a connection. This method should block the
     *  calling thread until the user either confirms or denies the request.
     *
     *  @param connectionName ip of the requested connection
     *  @param port Port of the requested connection
     *
     *  @return true is the user accepts the connection, false otherwise
     */
    public boolean requestConnection(String connectionName, int port) {
        ConnectionRequest request = new ConnectionRequest(connectionName, port);
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
     *  @param ip Internet Protocol Address of the connection to accept
     *  @param port Port of the connection to accept
     *  @param connectionName Name of the connection that will be accepted
     */
    public void acceptConnection(String ip, int port, String connectionName) {
        synchronized(pendingConnections) {
            for(ConnectionRequest cr: pendingConnections) {
                if(cr.getip().equals(ip) && cr.getPort() == port) {
                    cr.setAccepted(true);
                    synchronized(cr) {
                        cr.notify();
                    }
                    pendingConnections.remove(cr);
                    break;
                }
            }
        }
        if(connectionMap.get(ip) != null) {
            connectionMap.get(ip).setConnectionName(connectionName);
        }
    }
    
    /**
     *  Does not allow traffic to be received through the socket associated 
     *  with this Connection id
     * 
     *  @param connectionName Name of the connection to decline
     *  @param port Port of the connection to decline
     */
    public void declineConnection(String ip, int port) {
        synchronized(pendingConnections) {
            for(ConnectionRequest cr: pendingConnections) {
                if(cr.getip().equals(ip) && cr.getPort() == port) {
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
            ui.send(new ConnectionRefusedErrorPacket(e, connectionName, port));
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
        Connection newConnection = new Connection(s, connectionName, this);
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
     *  Sends a message to a connection
     * 
     *  @param connectionName Name of the connection to send messages
     *  @param message Message to send to a connection
     */
    public void sendMessage(String connectionName, String message) {
        connectionMap.get(connectionName).sendMessage(message);
    }
}
