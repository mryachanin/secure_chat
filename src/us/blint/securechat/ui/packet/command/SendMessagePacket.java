package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies that the user wants to send a message to a connection.
 */
public class SendMessagePacket extends Packet {
    private String connectionName, message;
    
    /**
     * Initializes variables
     * 
     *  @param connectionName   Name of the connection to send a message
     *  @param message          Message to send to a connection
     */
    public SendMessagePacket(String connectionName, String message) {
        this.connectionName = connectionName;
        this.message = message;
    }
    
    /**
     *  Returns the name of the connection to send a message
     * 
     *  @return connectionName
     */
    public String getConnectionName() {
        return connectionName;
    }
    
    /**
     *  Returns the message to send to a connection
     *  
     *  @return message
     */
    public String getMessage() {
        return message;
    }
}
