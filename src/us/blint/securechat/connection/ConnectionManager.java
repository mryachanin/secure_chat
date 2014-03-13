package us.blint.securechat.connection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import us.blint.securechat.client.Client;
import us.blint.securechat.ui.ChatInterface;
import us.blint.securechat.ui.packet.display.DisplayAcceptedConnectionsPacket;
import us.blint.securechat.ui.packet.display.DisplayPendingConnectionsPacket;
import us.blint.securechat.ui.packet.error.ConnectErrorPacket;
import us.blint.securechat.ui.packet.error.ConnectionRefusedErrorPacket;
import us.blint.securechat.ui.packet.error.DisconnectErrorPacket;

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
        connectionMap = new HashMap<String,Connection>();
    }

    /**
     *  Request the user to accept a connection. This method should block the
     *  calling thread until the user either confirms or denies the request.
     *
     *  @param connectionName The identifier for the connection
     *
     *  @return true is the user accepts the connection, false otherwise
     */
    public boolean requestConnection(String connectionName) {
        ConnectionRequest request = new ConnectionRequest(connectionName);
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
     *  Called with /accept
     *  
     *  Allows a connection to be instantiated
     * 
     *  @param connectionRequestName Name of the request to accept
     */
    public void acceptConnection(String connectionRequestName) {
        synchronized(pendingConnections) {
            for(ConnectionRequest cr: pendingConnections) {
                if(cr.getConnectionName().equals(connectionRequestName)) {
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
     *  Called with /decline
     *  
     *  Does not allow a connection to be instantiated
     * 
     *  @param connectionRequestName Name of the request to decline
     */
    public void declineConnection(String connectionName) {
        synchronized(pendingConnections) {
            for(ConnectionRequest cr: pendingConnections) {
                if(cr.getConnectionName().equals(connectionName)) {
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
     *  Called with /connect
     *  
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
     *  @param s Socket for a new connection
     *  @param connectionName Name of the connection for ease of reference
     */
    public void connect(Socket s, String connectionName) {
        Connection newConnection = new Connection(s, connectionName, this, ui);
        connectionMap.put(connectionName, newConnection);
        connections.add(newConnection);
    }
    
    /**
     *  Called with /disconnect
     *  
     *  Disconnect a current connection
     *  
     *  @param connectionName Name of the connection to disconnect from
     */
    public void disconnect(String connectionName) {
        try {
            connections.remove(connectionMap.get(connectionName));
        } catch(Exception e) {
            ui.send(new DisconnectErrorPacket(e));
        }
    }
    
    /**
     *  Connection requests from other clients
     * 
     *  @return ArrayList of connectionRequest objects that represent 
     *          current pending connections
     */
//    public ArrayList<ConnectionRequest> getPendingConnections() {
//        return pendingConnections;
//    }
    
    /**
     *  Prints the names of all currently accepted connections
     */
    public void printAcceptedConnections() {
        ui.send(new DisplayAcceptedConnectionsPacket(connections));
    }
    
    /**
     *  Prints the names of all pending connections
     */
    public void printPendingConnections() {
        ui.send(new DisplayPendingConnectionsPacket(pendingConnections));
    }
    
    /**
     *  Continuously sends messages to a connection until user types '/close'
     * 
     *  @param connectionName Name of the connection to send messages
     */
    public void sendMessage(String connectionName, String message) {
        Connection c = connectionMap.get(connectionName);
        c.sendMessage(message);
            

    }
}
