package us.blint.securechat.connection;
/**
 *  Stores the ID and state of a pending request.
 */
public class ConnectionRequest {
    private int connectionID;
    private boolean accept;
    
    /**
     *  Initialize variables
     * 
     *  @param connectionID   ID of the connection associated with this request.
     */
    public ConnectionRequest(int connectionID) {
        this.connectionID = connectionID;
    }
    
    /**
     *  Sets the boolean value signifying if the user has accepted this connection
     * 
     *  @param isAccepted   Boolean signifying if the user has accepted this connection
     */
    public void setAccepted(boolean isAccepted) {
        this.accept = isAccepted;
    }
    
    /**
     *  Returns whether or not the user has accepted this connection
     * 
     *  @return accept
     */
    public boolean isAccepted() {
        return accept;
    }
    
    /**
     *  Returns the ID of the connection this request is associated with
     *  
     *  @return connectionID
     */
    public int getConnectionID() {
        return connectionID;
    }
}
