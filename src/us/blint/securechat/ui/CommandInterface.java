package us.blint.securechat.ui;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import us.blint.securechat.connection.Connection;
import us.blint.securechat.connection.ConnectionRequest;
import us.blint.securechat.ui.packet.Packet;
import us.blint.securechat.ui.packet.command.AcceptConnectionPacket;
import us.blint.securechat.ui.packet.command.DeclineConnectionPacket;
import us.blint.securechat.ui.packet.command.DisconnectPacket;
import us.blint.securechat.ui.packet.command.PrintConnectionsPacket;
import us.blint.securechat.ui.packet.command.PrintPendingConnectionsPacket;
import us.blint.securechat.ui.packet.command.RequestConnectionPacket;
import us.blint.securechat.ui.packet.command.SendMessagePacket;
import us.blint.securechat.ui.packet.display.DisplayAcceptedConnectionsPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionAcceptedPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionDeclinedPacket;
import us.blint.securechat.ui.packet.display.DisplayMessagePacket;
import us.blint.securechat.ui.packet.display.DisplayPendingConnectionsPacket;
import us.blint.securechat.ui.packet.error.ConnectErrorPacket;
import us.blint.securechat.ui.packet.error.ConnectionRefusedErrorPacket;
import us.blint.securechat.ui.packet.error.DisconnectErrorPacket;


/**
 *  Interface meant to be used with the command line
 */
public class CommandInterface implements ChatInterface {
    
    BufferedReader in;
    PrintWriter out;
    
    /**
     *  Initializes the default input and output stream (system.in / system.out)
     */
    public CommandInterface() {
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out, true);
    }
    
    @Override
    public Packet getInput() {
        LinkedList<String> inputArray;
        try {
            inputArray = new LinkedList<String>(Arrays.asList(in.readLine().toLowerCase().split("\\s+")));
            switch(inputArray.pop()) {
                case "/accept":
                    return new AcceptConnectionPacket(inputArray.pop());
        
                case "/connect":
                    try {
                        return new RequestConnectionPacket(inputArray.pop(), 
                                    Integer.parseInt(inputArray.pop()), 
                                    inputArray.pop());
                        
                    } catch(NumberFormatException e) {
                        out.println("Usage: connect <ip> <port> <name>");
                    }
                    break;
        
                case "/decline":
                    return new DeclineConnectionPacket(inputArray.pop());
        
                case "/disconnect":
                    return new DisconnectPacket(inputArray.pop());
        
                case "/list":
                    return new PrintConnectionsPacket();
           
                case "/msg":
                    String connectionName = inputArray.pop();
                    while(true) {
                        String msg = in.readLine();
                        if(msg.equals("/close"))
                            break;
                        return new SendMessagePacket(connectionName, msg);
                    }
                
                case "/pending":
                    return new PrintPendingConnectionsPacket();
                    
                case "/quit":
                    System.exit(0);
                    
                default:
                    out.println("Valid commands:");
                    out.println("\t/accept <ip>");
                    out.println("\t/connect <ip> <port> <nickname>");
                    out.println("\t/decline <ip>");
                    out.println("\t/disconnect <ip/nickname>");
                    out.println("\t/list");
                    out.println("\t/msg <ip/nickname>");
                    out.println("\t/pending");
                    out.println("\t/quit");
                    return new Packet();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Packet();
    }

    @Override
    public void send(Packet p) {
    	if(p instanceof DisplayMessagePacket) {
    		out.println("#### Incomming request from " + ((DisplayMessagePacket)p).getConnectionName() + " ####");
    		out.println(((DisplayMessagePacket)p).getMessage());
    	}
    	
    	else if(p instanceof DisplayAcceptedConnectionsPacket) {
        	out.println("#### Connected To ####");
            ArrayList<Connection> connections = ((DisplayAcceptedConnectionsPacket)p).getAcceptedConnections();
            synchronized(connections) {
                for(Connection con: connections) {
                    if(con.isClosed())
                        connections.remove(con);
                    else
                        out.println(con.getName());
                }
            }
        }
        
    	else if(p instanceof DisplayPendingConnectionsPacket) {
        	out.println("#### Pending Connections ####");
            ArrayList<ConnectionRequest> pendingConnections = ((DisplayPendingConnectionsPacket)p).getPendingConnections();
            synchronized(pendingConnections) {
                for(ConnectionRequest cr: pendingConnections) {
                    out.println(cr.getConnectionName());
                }
            }
        }
        
        
    	else if(p instanceof DisplayConnectionAcceptedPacket) {
        	out.println("#### You are now connected to " + ((DisplayConnectionAcceptedPacket)p).getConnectionName() + " ####");
        }
        
    	else if(p instanceof DisplayConnectionDeclinedPacket) {
        	out.println("#### Declined connection from " + ((DisplayConnectionDeclinedPacket)p).getConnectionName() + " ####");
        }
        
    	else if(p instanceof ConnectionRefusedErrorPacket) {
        	out.println("#### "+ ((ConnectionRefusedErrorPacket)p).getConnectionName() + " declined your connection request ####");
        }
        
    	else if(p instanceof ConnectErrorPacket) {
        	out.println("Usage: /connect <ip> <port> <name>");
        }
        
    	else if(p instanceof DisconnectErrorPacket) {
        	out.println("Usage: /disconnect <ip/nickname>");
        }
        
    	else 
    		out.println(p);
    }
}
