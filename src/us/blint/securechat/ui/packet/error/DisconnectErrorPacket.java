package us.blint.securechat.ui.packet.error;

/**
 *  Signifies that there was an error while removing a connection from the
 *  hashMap of connections
 */
public class DisconnectErrorPacket extends ErrorPacket {

    /**
     *  Initialize variables
     *  
     *  @param e   Exception related to this error
     */
    public DisconnectErrorPacket(Exception e) {
        super(e);
    }
    
}
