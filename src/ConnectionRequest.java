
public class ConnectionRequest {
    private String connectionName;
    private boolean accept;
    
    public ConnectionRequest(String connectionName) {
        this.connectionName = connectionName;
    }
    public void setAccepted(boolean isAccepted) {
        this.accept = isAccepted;
    }
    public boolean isAccepted() {
        return accept;
    }
    public boolean equals(String connectionName) {
        return this.connectionName.equals(connectionName);
    }
    public String toString() {
        return this.connectionName;
    }
}
