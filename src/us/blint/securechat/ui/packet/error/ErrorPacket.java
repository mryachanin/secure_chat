package us.blint.securechat.ui.packet.error;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Represents a generic error
 *  
 *  All other error packets extend this packet
 */
public class ErrorPacket extends Packet {
    private Exception e;
    
    /**
     *  Initialize variables
     *  
     *  @param e   Exception related to this error
     */
    public ErrorPacket(Exception e) {
        this.e = e;
    }
    
    /**
     *  Returns the exception related to this error
     * 
     *  @return e
     */
    public Exception getException() {
        return e;
    }
}
