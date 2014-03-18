package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies that the user has accepted a connection.
 *  Contains the IP, port, and name of the connection accepted.
 *
 */
public class AcceptConnectionPacket extends Packet {
    private String ip, connectionName;
    private int port;
    
    /**
     *  Initializes variables
     * 
     *  @param ip               IP of the connection accepted
     *  @param port             Port of the connection accepted
     *  @param connectionName   Name of the connection accepted
     */
    public AcceptConnectionPacket(String ip, int port, String connectionName) {
        this.ip = ip;
        this.port = port;
        this.connectionName = connectionName;
    }
    
    /**
     *  Returns the IP address of the connection accepted
     *  
     *  @return ip
     */
    public String getip() {
        return ip;
    }
    
    /**
     *  Returns the port of the connection accepted
     *  
     *  @return port
     */
    public int getPort() {
        return port;
    }
    
    /**
     *  Returns the name of the connection accepted
     *  
     *  @return connectionName
     */
    public String getConnectionName() {
        return connectionName;
    }
}
