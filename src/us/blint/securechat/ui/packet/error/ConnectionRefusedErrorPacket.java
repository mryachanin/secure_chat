package us.blint.securechat.ui.packet.error;

public class ConnectionRefusedErrorPacket extends ErrorPacket {
	private String connectionName;
	
    public ConnectionRefusedErrorPacket(Exception e, String connectionName) {
        super(e);
        this.connectionName = connectionName;
    }
    
    public String getConnectionName() {
    	return connectionName;
    }
}
