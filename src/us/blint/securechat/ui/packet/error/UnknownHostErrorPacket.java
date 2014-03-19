package us.blint.securechat.ui.packet.error;

/**
 *  Not really sure... 
 */
public class UnknownHostErrorPacket extends ErrorPacket {

    private int connectionID;
    
    /**
     *  Initialize variables
     *  
     *  @param e              Exception related to this error
     *  @param connectionID   ID of the Connection related to the unknown host
     */
    public UnknownHostErrorPacket(Exception e, int connectionID) {
        super(e);
        this.connectionID = connectionID;
    }
    
    /**
     *  Returns the ID of the connection refused
     *  
     *  @return connectionID
     */
    public int getConnectionID() {
        return connectionID;
    }
}
