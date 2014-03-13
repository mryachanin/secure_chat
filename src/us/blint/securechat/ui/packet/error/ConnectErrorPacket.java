package us.blint.securechat.ui.packet.error;

public class ConnectErrorPacket extends ErrorPacket {

    public ConnectErrorPacket(Exception e) {
        super(e);
    }
    
}
