package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies to the user interface that a message has been received.
 */
public class DisplayMessagePacket extends Packet {
    private String message, connectionName;
    
    /**
     *  Initialize variables
     *  
     *  @param message          Message received
     *  @param connectionName   Name of the connection that sent the message
     */
    public DisplayMessagePacket(String message, String connectionName) {
        this.message = message;
        this.connectionName = connectionName;
    }
    
    /**
     *  Returns the message received by the connection
     *  
     *  @return message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     *  Returns the name of the connection that sent the message
     *  
     *  @return connectionName
     */
    public String getConnectionName() {
    	return connectionName;
    }
}
