package us.blint.securechat.connection;
/**
 *  Stores the name, number, and state of a pending request.
 */
public class ConnectionRequest {
    private String connectionName;
    private int connectionNumber;
    private boolean accept;
    
    /**
     *  Initializes connection name to value passed in
     * 
     *  @param connectionName   Name of the connection associated with this request.
     *  @param connectionNumber Unique number of the connection this request is associated with
     */
    public ConnectionRequest(String connectionName, int connectionNumber) {
        this.connectionName = connectionName;
        this.connectionNumber = connectionNumber;
    }
    
    /**
     *  Sets the boolean value signifying if the user has accepted this connection
     * 
     *  @param isAccepted Boolean signifying if the user has accepted this connection
     */
    public void setAccepted(boolean isAccepted) {
        this.accept = isAccepted;
    }
    
    /**
     *  Returns whether or not the user has accepted this connection
     * 
     *  @return Boolean signifying if the user has accepted this connection
     */
    public boolean isAccepted() {
        return accept;
    }
    
    /**
     *  Returns the name of the connection this request is associated with
     * 
     *  @return Name of the connection this request is associated with
     */
    public String getConnectionName() {
        return connectionName;
    }
    
    /**
     *  Returns the unique number of the connection this request is associated with
     *  
     *  @return Unique number of the connection this request is associated with
     */
    public int getConnectionNumber() {
        return connectionNumber;
    }
}
