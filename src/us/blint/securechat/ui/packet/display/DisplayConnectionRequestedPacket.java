package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

public class DisplayConnectionRequestedPacket extends Packet {
	int connectionID;
	
	public DisplayConnectionRequestedPacket(int connectionID) {
		this.connectionID = connectionID;
	}
	
	public int getConnectionID() {
		return connectionID;
	}
}
