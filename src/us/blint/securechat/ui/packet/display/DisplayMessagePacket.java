package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies to the user interface that a message has been received.
 */
public class DisplayMessagePacket extends Packet {
    private String message;
    private int connectionID;
    
    /**
     *  Initialize variables
     *  
     *  @param message        Message received
     *  @param connectionID   ID of the connection that sent the message
     */
    public DisplayMessagePacket(String message, int connectionID) {
        this.message = message;
        this.connectionID = connectionID;
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
     *  Returns the ID of the connection that sent the message
     *  
     *  @return connectionID
     */
    public int getConnectionID() {
    	return connectionID;
    }
}
