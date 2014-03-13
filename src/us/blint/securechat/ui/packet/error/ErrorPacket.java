package us.blint.securechat.ui.packet.error;

import us.blint.securechat.ui.packet.Packet;

/**
 *  Represents an error
 *
 */
public class ErrorPacket extends Packet {
    private Exception e;
    
    public ErrorPacket(Exception e) {
        this.e = e;
    }
    
    /**
     *  Returns the exception that caused the error
     * 
     * @return Exception thrown
     */
    public Exception getException() {
        return e;
    }
}
