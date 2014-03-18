package us.blint.securechat.connection;
/**
 *  Stores the name, number, and state of a pending request.
 */
public class ConnectionRequest {
    private String ip;
    private int port;
    private boolean accept;
    
    /**
     *  Initialize variables
     * 
     *  @param ip     ip of the connection associated with this request.
     *  @param port   Port of the connection associated with this request
     */
    public ConnectionRequest(String ip, int port) {
        this.ip = ip;
        this.port = port;
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
     *  Returns the IP of the connection this request is associated with
     *  
     *  @return ip
     */
    public String getip() {
        return ip;
    }
    
    /**
     *  Returns the port of the connection this request is associated with
     * 
     *  @return port
     */
    public int getPort() {
        return port;
    }
}
