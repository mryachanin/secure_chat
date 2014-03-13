package us.blint.securechat.ui.packet.display;

import us.blint.securechat.ui.packet.Packet;

public class DisplayServerStartPacket extends Packet {
	int port;
	
	public DisplayServerStartPacket(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return port;
	}
}
