package us.blint.securechat.connection;
/**
 *  Stores the name, number, and state of a pending request.
 */
public class ConnectionRequest {
    private String ip;
    private int port;
    private boolean accept;
    
    /**
     *  Initializes connection name to value passed in
     * 
     *  @param connectionName   Name of the connection associated with this request.
     *  @param connectionNumber Unique number of the connection this request is associated with
     */
    public ConnectionRequest(String ip, int port) {
        this.ip = ip;
        this.port = port;
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
     *  Returns the ip of the connection this request is associated with
     *  
     *  @return ip of the connection this request is associated with
     */
    public String getip() {
        return ip;
    }
    
    /**
     *  Returns the port of the connection this request is associated with
     * 
     *  @return Port of the connection this request is associated with
     */
    public int getPort() {
        return port;
    }
}
