package us.blint.securechat.client;

import us.blint.securechat.connection.ConnectionManager;
import us.blint.securechat.ui.ChatInterface;
import us.blint.securechat.ui.CommandInterface;
import us.blint.securechat.ui.packet.Packet;
import us.blint.securechat.ui.packet.command.AcceptConnectionPacket;
import us.blint.securechat.ui.packet.command.DeclineConnectionPacket;
import us.blint.securechat.ui.packet.command.DisconnectPacket;
import us.blint.securechat.ui.packet.command.RequestConnectionPacket;
import us.blint.securechat.ui.packet.command.SendMessagePacket;
import us.blint.securechat.server.Server;

/**
 *  Defines a client that can accept connections, request connections,
 *      and send data securely over connections to other clients. 
 *      
 *  When this thread starts, it will:
 *      Continuously probe for command packets from the user interface and deal
 *      with them accordingly
 */
public class Client extends Thread {

    private ChatInterface ui;
    private ConnectionManager cm;
    private Server server;
    
    /**
     *  Initialize variables
     *  Starts server thread
     *  Starts main thread
     *  
     *  @param ui   String representation of the user interface class to use
     */
    public Client(String uiClassName) {
        ui= new CommandInterface();
        cm = ConnectionManager.getInstance();
        cm.setUI(ui);
        server = new Server();
        server.start();
        start();
    }
    
    @Override
    public void run() {
        Packet command;
        while((command = ui.getInput()) != null) {
            if(command instanceof AcceptConnectionPacket) {
                cm.acceptConnection(((AcceptConnectionPacket)command).getip(), ((AcceptConnectionPacket)command).getPort(), ((AcceptConnectionPacket)command).getConnectionName());
            }
            
            else if(command instanceof RequestConnectionPacket) {
                cm.connect(((RequestConnectionPacket)command).getip(), ((RequestConnectionPacket)command).getPort(), ((RequestConnectionPacket)command).getConnectionName());
            }
            
            else if(command instanceof DeclineConnectionPacket) {
                cm.declineConnection(((DeclineConnectionPacket)command).getip(), ((DeclineConnectionPacket)command).getPort());
            }
            
            else if(command instanceof DisconnectPacket) {
                cm.disconnect(((DisconnectPacket)command).getConnectionName());
            }
            
            else if(command instanceof SendMessagePacket) {
                cm.sendMessage(((SendMessagePacket)command).getConnectionName(), ((SendMessagePacket)command).getMessage());
            }
        }
    }
    
    /**
     *  Instantiates a new client using the interface name passed in
     *  
     *  @param args
     *          1) name of the user interface to use
     *          2) null
     */
    public static void main(String[] args) {
        if(args.length < 1)
            new Client("us.blint.securechat.ui.CommandInterface");
        else
            new Client("us.blint.securechat.ui." + args[1]);
    }
}
