package us.blint.securechat.client;

import us.blint.securechat.connection.ConnectionManager;
import us.blint.securechat.ui.ChatInterface;
import us.blint.securechat.ui.CommandInterface;
import us.blint.securechat.ui.packet.Packet;
import us.blint.securechat.ui.packet.command.AcceptConnectionPacket;
import us.blint.securechat.ui.packet.command.DeclineConnectionPacket;
import us.blint.securechat.ui.packet.command.DisconnectPacket;
import us.blint.securechat.ui.packet.command.PrintConnectionsPacket;
import us.blint.securechat.ui.packet.command.PrintPendingConnectionsPacket;
import us.blint.securechat.ui.packet.command.RequestConnectionPacket;
import us.blint.securechat.ui.packet.command.SendMessagePacket;
import us.blint.securechat.server.Server;

/**
 *  Defines a client that can accept connections, request connections,
 *      and send data securely over connections to other clients. 
 *      
 *  When this thread starts, it will:
 *      Continuously listen for commands and execute them
 */
public class Client extends Thread {
    
    private static ChatInterface ui;
    //private static void setChatInteface(ChatInterface ui) {
    //    Client.ui = ui;
    //}
    public static ChatInterface getChatInteface() {
        return Client.ui;
    }
    
    private ConnectionManager cm;
    private Server server;

    /**
     *  Initializes variables
     *  Starts server thread
     *  Starts main thread
     *  
     *  @param ui Defines the user interface
     */
    public Client(String uiClassName) {
        this.cm = ConnectionManager.getInstance();
        //Class<?> chatInterface = Class.forName(uiClassName);
        //Object ui = chatInterface.newInstance();
        ui= new CommandInterface();
        server = new Server();
        server.start();
        start();
    }
    
    @Override
    public void run() {
        Packet command;
        while((command = ui.getInput()) != null) {
            if(command instanceof AcceptConnectionPacket) {
                cm.acceptConnection(((AcceptConnectionPacket)command).getConnectionName());
                break;
            }
            
            else if(command instanceof RequestConnectionPacket) {
                cm.connect(((RequestConnectionPacket)command).getip(), ((RequestConnectionPacket)command).getPort(), ((RequestConnectionPacket)command).getConnectionName());
                break;
            }
            
            else if(command instanceof DeclineConnectionPacket) {
                cm.declineConnection(((DeclineConnectionPacket)command).getConnectionName());
                break;
            }
            
            else if(command instanceof DisconnectPacket) {
                cm.acceptConnection(((DisconnectPacket)command).getConnectionName());
                break;
            }
            
            else if(command instanceof PrintConnectionsPacket) {
                cm.printAcceptedConnections();
                break;
            }
            
            else if(command instanceof SendMessagePacket) {
                cm.sendMessage(((SendMessagePacket)command).getConnectionName(), ((SendMessagePacket)command).getMessage());
                break;
            }
            
            else if(command instanceof PrintPendingConnectionsPacket) {
                cm.printPendingConnections();
                break;
            }
        }
    }
    
    /**
     *  Instantiates a new client using the command interface
     *  
     *  @param args These will all be ignored
     */
    public static void main(String[] args) {
        if(args.length < 1)
            new Client("us.blint.securechat.ui.CommandInterface");
        else
            new Client("us.blint.securechat.ui." + args[1]);
    }
}
