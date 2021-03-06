package us.blint.securechat.ui.packet.error;

/**
 *  Signifies that for some reason, the connection attempted was refused.
 */
public class ConnectionRefusedErrorPacket extends ErrorPacket {
	private int connectionID;
	
    /**
     *  Initialize variables
     *  
     *  @param e              Exception related to this error
     *  @param connectionID   ID of the connection refused
     */
    public ConnectionRefusedErrorPacket(Exception e, int connectionID) {
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
