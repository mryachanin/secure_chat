package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

public class DisplayConnectionRequestPacket extends Packet {
	String connectionName;
	
	public DisplayConnectionRequestPacket(String connectionName) {
		this.connectionName = connectionName;
	}
	
	public String getConnectionName() {
		return connectionName;
	}
}
