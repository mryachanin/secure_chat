/**
 *  Stores the name of a pending request and the state of that request.
 */
public class ConnectionRequest {
    private String connectionName;
    private boolean accept;
    
    /**
     *  Initializes connection name to value passed in
     * 
     *  @param connectionName Name of the connection associated with this request.
     */
    public ConnectionRequest(String connectionName) {
        this.connectionName = connectionName;
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
     *  Checks if the string passed in is equal to this connection name
     *  
     *  @param connectionName Name of the connection associated with this request.
     *  @return true: connectionName = this.name; else false
     */
    public boolean equals(String connectionName) {
        return this.connectionName.equals(connectionName);
    }
    
    /**
     *  Returns the name of the connection this request is associated with
     * 
     *  @return Name of the connection this request is associated with
     */
    public String getConnectionName() {
        return this.connectionName;
    }
}
