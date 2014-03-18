package us.blint.securechat.ui.packet.error;

/**
 *  Not really sure... 
 */
public class ConnectIOErrorPacket extends ErrorPacket {

    /**
     *  Initialize variables
     *  
     *  @param e   Exception related to this error
     */
    public ConnectIOErrorPacket(Exception e) {
        super(e);
    }
    
}
