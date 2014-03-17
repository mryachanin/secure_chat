package us.blint.securechat.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import us.blint.securechat.ui.packet.Packet;
import us.blint.securechat.ui.packet.command.AcceptConnectionPacket;
import us.blint.securechat.ui.packet.command.DeclineConnectionPacket;
import us.blint.securechat.ui.packet.command.DisconnectPacket;
import us.blint.securechat.ui.packet.command.RequestConnectionPacket;
import us.blint.securechat.ui.packet.command.SendMessagePacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionAcceptedPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionDeclinedPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionRequestPacket;
import us.blint.securechat.ui.packet.display.DisplayMessagePacket;
import us.blint.securechat.ui.packet.display.DisplayServerStartPacket;
import us.blint.securechat.ui.packet.error.ConnectErrorPacket;
import us.blint.securechat.ui.packet.error.ConnectionRefusedErrorPacket;
import us.blint.securechat.ui.packet.error.DisconnectErrorPacket;


/**
 *  Interface meant to be used with the command line
 */
public class CommandInterface implements ChatInterface {
    
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<String> pendingConnections;
    private ArrayList<String> acceptedConnections;
    
    /**
     *  Initializes the default input and output stream (system.in / system.out)
     */
    public CommandInterface() {
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out, true);
        pendingConnections = new ArrayList<String>();
        acceptedConnections = new ArrayList<String>();
    }
    
    @Override
    public Packet getInput() {
        LinkedList<String> inputArray;
        String ip, port, connectionName;
        try {
            inputArray = new LinkedList<String>(Arrays.asList(in.readLine().toLowerCase().split("\\s+")));
            switch(inputArray.pop()) {
                case "/accept":
                    ip = inputArray.pop();
                    port = inputArray.pop();               
                    connectionName = inputArray.pop();
                    
                    acceptedConnections.add(connectionName + ":" + port);
                    pendingConnections.remove(ip + ":" + port);
                    
                    try {
                        return new AcceptConnectionPacket(ip, Integer.parseInt(port), connectionName);
                    } catch(NumberFormatException e) {
                        out.println("Usage: accept <ip> <port> <nickname>");
                        return new Packet();
                    }
        
                case "/connect":
                    ip = inputArray.pop();
                    port = inputArray.pop();
                    connectionName = inputArray.pop();
                    
                    pendingConnections.add(connectionName + ":" + port);
                    
                    try {
                        return new RequestConnectionPacket(ip, Integer.parseInt(port), connectionName);
                    } catch(NumberFormatException e) {
                        out.println("Usage: connect <ip> <port> <nickname>");
                        return new Packet();
                    }
        
                case "/decline":
                    ip = inputArray.pop();
                    port = inputArray.pop();                 
                    
                    pendingConnections.remove(ip + ":" + port);
                    
                    try {
                        return new DeclineConnectionPacket(ip, Integer.parseInt(port));
                    } catch(NumberFormatException e) {
                        out.println("Usage: decline <ip> <port>");
                        return new Packet();
                    }
        
                case "/disconnect":
                    connectionName = inputArray.pop();
                    return new DisconnectPacket(connectionName);
        
                case "/list":
                    out.println("#### Connected To ####");
                    for(String con: acceptedConnections) {
                        out.println(con);
                    }
                    break;
           
                case "/msg":
                    connectionName = inputArray.pop();
                    String message = in.readLine();
                    return new SendMessagePacket(connectionName, message);
                
                case "/pending":
                    out.println("#### Pending Connections ####");
                    for(String con: pendingConnections) {
                        out.println(con);
                    }
                    break;
                    
                case "/quit":
                    System.exit(0);
                    
                default:
                    out.println("Valid commands:");
                    out.println("\t/accept <ip> <port> <nickname>");
                    out.println("\t/connect <ip> <port> <nickname>");
                    out.println("\t/decline <ip> <port>");
                    out.println("\t/disconnect <nickname>");
                    out.println("\t/list");
                    out.println("\t/msg <nickname>");
                    out.println("\t/pending");
                    out.println("\t/quit");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Packet();
    }

    @Override
    public void send(Packet p) {
        String ip, connectionName;
        int port;
    	if(p instanceof DisplayMessagePacket) {
    	    connectionName = ((DisplayMessagePacket)p).getConnectionName();
    		out.println("#### Incomming from " + connectionName + " ####");
    		out.println(((DisplayMessagePacket)p).getMessage());
    	}    
        
    	else if(p instanceof DisplayConnectionAcceptedPacket) {
    	    connectionName = ((DisplayConnectionAcceptedPacket)p).getConnectionName();
        	out.println("#### You are now connected to " + connectionName + " ####");
        }
        
    	else if(p instanceof DisplayConnectionDeclinedPacket) {
    	    ip = ((DisplayConnectionDeclinedPacket)p).getip();
    	    port = ((DisplayConnectionDeclinedPacket)p).getPort();
        	out.println("#### Declined connection from " + ip + ":" + port + " ####");
        }
    	
    	else if(p instanceof DisplayServerStartPacket) {
    	    port = ((DisplayServerStartPacket)p).getPort();
    		out.println("#### Server started on port: " + port + " ####");
    	}
    	
    	else if(p instanceof DisplayConnectionRequestPacket) {
    	    ip = ((DisplayConnectionRequestPacket)p).getip();
    	    port = ((DisplayConnectionRequestPacket)p).getPort();
    		out.println("#### Incoming Request From " + ip + ":" + port + " ####");
    	}
        
    	else if(p instanceof ConnectionRefusedErrorPacket) {
        	connectionName = ((ConnectionRefusedErrorPacket)p).getConnectionName();
        	port = ((ConnectionRefusedErrorPacket)p).getPort();
    	    out.println("#### "+ connectionName + ":" + port + " declined your connection request ####");
        	pendingConnections.remove(connectionName + ":" + port);
    	}
        
    	else if(p instanceof ConnectErrorPacket) {
        	out.println("Usage: /connect <ip> <port> <name>");
        }
        
    	else if(p instanceof DisconnectErrorPacket) {
        	out.println("Usage: /disconnect <ip/nickname>");
        }
    }
}
