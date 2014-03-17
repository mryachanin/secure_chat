package us.blint.securechat.ui.packet.error;

public class ConnectionRefusedErrorPacket extends ErrorPacket {
	private String connectionName;
	private int port;
	
    public ConnectionRefusedErrorPacket(Exception e, String connectionName, int port) {
        super(e);
        this.connectionName = connectionName;
        this.port = port;
    }
    
    public String getConnectionName() {
    	return connectionName;
    }
    
    public int getPort() {
        return port;
    }
}
