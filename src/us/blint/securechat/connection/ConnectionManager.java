package us.blint.securechat.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import us.blint.securechat.ui.ChatInterface;
import us.blint.securechat.ui.packet.display.DisplayConnectionNameExistsPacket;
import us.blint.securechat.ui.packet.error.ConnectionRefusedErrorPacket;
import us.blint.securechat.ui.packet.error.DisconnectErrorPacket;

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
    private HashMap<String,Connection> connectionMap;
    
    private ConnectionManager() {
        pendingConnectionRequests = new ArrayList<ConnectionRequest>();
        connectionMap = new HashMap<String,Connection>();
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
     *  @param ip     IP of the requested connection
     *  @param port   Port of the requested connection
     *
     *  @return true if the user accepts the connection, false otherwise
     */
    public boolean requestConnection(String ip, int port) {
        ConnectionRequest request = new ConnectionRequest(ip, port);
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
     *  Allows messages to be received through the socket associated with this
     *  IP and port.
     *  Assigns a name to the connection if one is provided.
     * 
     *  @param ip               IP address of the connection to accept
     *  @param port             Port of the connection to accept
     *  @param connectionName   Name of the connection that will be accepted
     */
    public void acceptConnection(String ip, int port, String connectionName) {
        if(connectionName != null && connectionMap.containsKey(connectionName)) {
            ui.send(new DisplayConnectionNameExistsPacket(connectionName));
            return;
        }
        
        synchronized(pendingConnectionRequests) {
            for(ConnectionRequest cr: pendingConnectionRequests) {
                if(cr.getip().equals(ip) && cr.getPort() == port) {
                    cr.setAccepted(true);
                    synchronized(cr) {
                        cr.notify();
                    }
                    pendingConnectionRequests.remove(cr);
                    break;
                }
            }
        }
        if(connectionName != null && connectionMap.get(ip) != null) {
            connectionMap.get(ip).setConnectionName(connectionName);
        }
    }
    
    /**
     *  Does not allow traffic to be received through the socket associated 
     *  with this IP and port
     * 
     *  @param ip     IP address of the connection to decline
     *  @param port   Port of the connection to decline
     */
    public void declineConnection(String ip, int port) {
        synchronized(pendingConnectionRequests) {
            for(ConnectionRequest cr: pendingConnectionRequests) {
                if(cr.getip().equals(ip) && cr.getPort() == port) {
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
     *  Instantiates a new connection that was requested by the Client
     *  Associates the connection with a name
     *  Adds the connection to hashMap with key:value = connection_name : Connection
     *  
     *  @param ip               IP Address of the socket for a new connection
     *  @param port             Port of the socket for a new connection
     *  @param connectionName   Name of the connection for ease of reference
     */
    public void connect(String ip, int port, String connectionName) {
        try {
            Socket s = new Socket(ip, port);
            Connection newConnection = new Connection(s, connectionName, this, ui, true);
            connectionMap.put(connectionName, newConnection);
        } catch (UnknownHostException e) {
            System.out.println("Not a known host, you're dumb");
            return;
        } catch(IOException e) {
            ui.send(new ConnectionRefusedErrorPacket(e, connectionName, port));
        }
    }

    /**
     *  Instantiates a new connection that was requested by a different Client
     *  Associates the connection with a name
     *  Adds the connection to hashMap with key:value = connection_name : Connection
     * 
     *  @param  s                Socket for a new connection
     *  @param  connectionName   Name of the connection for ease of reference
     */
    public void connect(Socket s, String connectionName) {
        Connection newConnection = new Connection(s, connectionName, this, ui, false);
        connectionMap.put(connectionName, newConnection);
    }
    
    /**
     *  Disconnect a current connection
     *  
     *  @param connectionName   Name of the connection to disconnect from
     */
    public void disconnect(String connectionName) {
        try {
            connectionMap.get(connectionName).close();
            connectionMap.remove(connectionName);
        } catch(Exception e) {
            ui.send(new DisconnectErrorPacket(e));
        }
    }
    
    /**
     *  Sends a message to a connection
     * 
     *  @param connectionName   Name of the connection to send messages
     *  @param message          Message to send to a connection
     */
    public void sendMessage(String connectionName, String message) {
        connectionMap.get(connectionName).sendMessage(message);
    }
}
