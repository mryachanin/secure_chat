package us.blint.securechat.ui.packet.error;

public class DisconnectErrorPacket extends ErrorPacket {

    public DisconnectErrorPacket(Exception e) {
        super(e);
    }
    
}
