package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies that the user wants to connect to another client.
 */
public class RequestConnectionPacket extends Packet {
    private String ip, connectionName;
    private int port;
    
    /**
     *  Initializes variables
     *  
     *  @param ip               IP of the server to connect to
     *  @param port             Port of the server to connect to
     *  @param connectionName   New name for this connection
     */
    public RequestConnectionPacket(String ip, int port, String connectionName) {
        this.ip = ip;
        this.port = port;
        this.connectionName = connectionName;
    }
    
    /**
     *  Returns the IP of the server to connect to
     *  
     *  @return ip
     */
    public String getip() {
        return ip;
    }
    
    /**
     *  Returns the port of the server to connect to
     *  
     *  @return port
     */
    public int getPort() {
        return port;
    }
    
    /**
     *  Returns the new name for this connection
     *  
     *  @return connectionName
     */
    public String getConnectionName() {
        return connectionName;
    }
}
