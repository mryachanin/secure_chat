package us.blint.securechat.ui.packet.error;

/**
 *  Signifies that for some reason, the connection attempted was refused.
 */
public class ConnectionRefusedErrorPacket extends ErrorPacket {
	private String ip;
	private int port;
	
    /**
     *  Initialize variables
     *  
     *  @param e      Exception related to this error
     *  @param ip     IP of the connection refused
     *  @param port   Port of the connection refused
     */
    public ConnectionRefusedErrorPacket(Exception e, String ip, int port) {
        super(e);
        this.ip = ip;
        this.port = port;
    }
    
    /**
     *  Returns the IP of the connection refused
     *  
     *  @return ip
     */
    public String getIP() {
    	return ip;
    }
    
    /**
     *  Returns the port of the connection refused
     *  
     *  @return port
     */
    public int getPort() {
        return port;
    }
}
