package us.blint.securechat.ui.packet.command;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Signifies that the user wants to send a message to a connection.
 */
public class SendMessagePacket extends Packet {
    private int connectionID;
    private String message;
    
    /**
     * Initializes variables
     * 
     *  @param connectionID   ID of the connection to send a message
     *  @param message        Message to send to a connection
     */
    public SendMessagePacket(int connectionID, String message) {
        this.connectionID = connectionID;
        this.message = message;
    }
    
    /**
     *  Returns the ID of the connection to send a message
     * 
     *  @return connectionID
     */
    public int getConnectionID() {
        return connectionID;
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
