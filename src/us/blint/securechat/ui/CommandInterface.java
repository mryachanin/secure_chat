package us.blint.securechat.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import us.blint.securechat.ui.packet.Packet;
import us.blint.securechat.ui.packet.command.AcceptConnectionPacket;
import us.blint.securechat.ui.packet.command.DeclineConnectionPacket;
import us.blint.securechat.ui.packet.command.DisconnectPacket;
import us.blint.securechat.ui.packet.command.RequestConnectionPacket;
import us.blint.securechat.ui.packet.command.SendMessagePacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionAcceptedPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionDeclinedPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionRequestPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionRequestedPacket;
import us.blint.securechat.ui.packet.display.DisplayMessagePacket;
import us.blint.securechat.ui.packet.display.DisplayServerStartPacket;
import us.blint.securechat.ui.packet.error.UnknownHostErrorPacket;
import us.blint.securechat.ui.packet.error.ConnectionRefusedErrorPacket;
import us.blint.securechat.ui.packet.error.DisconnectErrorPacket;


/**
 *  Interface meant to be used with the command line
 */
public class CommandInterface implements ChatInterface {
    
    private BufferedReader in;
    private PrintWriter out;
    private HashMap<String, ConnectionInfo> connectionMap;
    private int sendMessageID;
    
    private final String ACCEPT_COMMAND = "/accept <ip> <port> <nickname (optional)>";
    private final String CLOSE_COMMAND = "/close";
    private final String CONNECT_COMMAND = "/connect <ip> <port> <nickname (optional)>";
    private final String DECLINE_COMMAND = "/decline <ip> <port>";
    private final String DISCONNECT_COMMAND = "/disconnect <connection name>";
    private final String LIST_COMMAND = "/list";
    private final String MSG_COMMAND = "/msg <connection name>";
    private final String PENDING_COMMAND = "/pending";
    private final String QUIT_COMMAND = "/quit";
    private final String VALID_COMMANDS = "Valid commands:\n" + 
                                          "\t" + ACCEPT_COMMAND + "\n" + 
                                          "\t" + CONNECT_COMMAND + "\n" + 
                                          "\t" + DECLINE_COMMAND + "\n" + 
                                          "\t" + DISCONNECT_COMMAND + "\n" + 
                                          "\t" + LIST_COMMAND + "\n" + 
                                          "\t" + MSG_COMMAND + "\n" + 
                                          "\t" + PENDING_COMMAND + "\n" + 
                                          "\t" + QUIT_COMMAND;
    /**
     *  Initialize variables
     */
    public CommandInterface() {
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out, true);
        connectionMap = new HashMap<String, ConnectionInfo>();
        sendMessageID = -1;
    }
    
    /**
     *  Stores information about a connection for displaying to the user
     */
    private class ConnectionInfo {
        private int id, port;
        private String ip;
        private boolean accepted;
        
        /**
         *  Initialize variables
         * 
         *  @param ID     ID of a Connection
         *  @param ip     IP of a Connection
         *  @param port   Port of a Connection
         */
        public ConnectionInfo(String ip, int port, boolean accepted) {
            this.id = -1;
            this.port = port;
            this.ip = ip;
            this.accepted = accepted;
        }
        
        /**
         *  Sets if the connection has been accepted or not
         *  
         *  @param accepted   Signifies if the connection has been accepted
         */
        public void setAccepted(boolean accepted) {
            this.accepted = accepted;
        }
        
        /**
         *  Sets the ID of the connection this is related to
         * 
         *  @param id   New identifier of the connection this is related to
         */
        public void setID(int id) {
            this.id = id;
        }
        
        /**
         *  Returns the ID of the Connection this is related to
         *  
         *  @return id
         */
        public int getID() {
            return id;
        }
        
        /**
         *  Returns the IP of the Connection this is related to
         * 
         *  @return ip
         */
        public String getIP() {
            return ip;
        }
        
        /**
         *  Returns the port of the Connection this is related to
         *  
         *  @return port
         */
        public int getPort() {
            return port;
        }
        
        @Override
        public String toString() {
            return ip + ":" + port;
        }
    }
    
    @Override
    public Packet getInput() {
        try {
            String line = in.readLine();
            
            // Check to see if a command was typed
            if(line != null) {
	            // Check to see if  this a command or a message
	            if(line.charAt(0) != '/') {
	                // If sendMessageID == -1, the user has not tried to send a message
	                // Assume the user mistyped and return valid commands
	                if(sendMessageID == -1)
	                    out.println(VALID_COMMANDS);
	                else
	                    return new SendMessagePacket(sendMessageID, line);
	            }
	            
	            LinkedList<String> inputArray = new LinkedList<String>(Arrays.asList(line.split("\\s+")));
	            String command = inputArray.pop();
	            
	            String ip, connectionName;
	            int id, port;
	            
	            if(command.equals("/accept")) {
	                // This command takes 2 or 3 arguments
	                if(inputArray.size() > 1 && inputArray.size() < 4) {
	                    try {
	                        ip = inputArray.pop();
	                        port = Integer.parseInt(inputArray.pop());               
	                        
	                        // Check if connection actually exists
	                        if(connectionMap.get(ip + ":" + port) != null) {
	                            
	                            // Check if user typed a new name for the connection
	                            if(inputArray.size() > 0) {
	                                connectionName = inputArray.pop();
	                                
	                                // Check if that name already exists and loop until the user chooses an unused name
	                                while(connectionMap.containsKey(connectionName)) {
	                                    out.println("Either that connection name is already in use, or you typed a name with spaces. Please type another name.");
	                                    String newConnectionName = in.readLine();
	                                    
	                                    // Names must not have spaces
	                                    if(!newConnectionName.contains(" "))
	                                        connectionName = newConnectionName;
	                                }
	                                ConnectionInfo ci = connectionMap.get(ip + ":" + port);
	                                connectionMap.put(connectionName, ci);
	                                connectionMap.remove(ip + ":" + port);
	                                connectionMap.get(connectionName).setAccepted(true);
	                                id = connectionMap.get(connectionName).getID();
	                            }
	                            
	                            // else assume that the connection will be referred to by ip:port
	                            else {
	                                connectionMap.get(ip + ":" + port).setAccepted(true);
	                                id = connectionMap.get(ip + ":" + port).getID();
	                            }
	                            
	                            return new AcceptConnectionPacket(id);
	                        }
	                    } catch(NumberFormatException e) {}
	                }
	                out.println("Usage: " + ACCEPT_COMMAND);
	            }
	    
	            else if(command.equals("/close")) {
	                // This command takes 0 arguments
	                if(inputArray.size() == 0)
	                    sendMessageID = -1;
	                else
	                    out.println("Usage: " + CLOSE_COMMAND);
	            }
	            
	            else if(command.equals("/connect")) {
	                // This command takes 2 or 3 arguments
	                if(inputArray.size() > 1 && inputArray.size() < 4) {
	                    try {
	                        ip = inputArray.pop();
	                        port = Integer.parseInt(inputArray.pop());
	                        
	                        // Check if user typed a name for the connection
	                        if(inputArray.size() > 0) {
	                            connectionName = inputArray.pop();
	                            
	                            // Check if that name already exists and loop until the user chooses an unused name
	                            while(connectionMap.containsKey(connectionName)) {
	                                out.println("Either that connection name is already in use, or you typed a name with spaces. Please type another name.");
	                                String newConnectionName = in.readLine();
	                                
	                                // Names must not have spaces
	                                if(!newConnectionName.contains(" "))
	                                    connectionName = newConnectionName;
	                            }
	                            connectionMap.put(connectionName, new ConnectionInfo(ip, port, false));
	                        }
	                        else 
	                            connectionMap.put(ip + ":" + port, new ConnectionInfo(ip, port, false));
	                        
	                        return new RequestConnectionPacket(ip, port);
	                        
	                    } catch(NumberFormatException e) {}
	                }
	                out.println("Usage: " + CONNECT_COMMAND);
	            }
	    
	            
	            else if(command.equals("/decline")) {
	                // This command takes 2 arguments
	                if(inputArray.size() == 2) {
	                    try {
	                        ip = inputArray.pop();
	                        port = Integer.parseInt(inputArray.pop());
	                        
	                        // Check if connection actually exists
	                        if(connectionMap.get(ip + ":" + port) != null) {
	                            id = connectionMap.get(ip + ":" + port).getID();
	                            return new DeclineConnectionPacket(id);
	                        }
	                    } catch(NumberFormatException e) {}
	                }
	                out.println("Usage: " + DECLINE_COMMAND);
	            }
	    
	            
	            else if(command.equals("/disconnect")) {
	                // This command takes 1 argument
	                if(inputArray.size() == 1) {
	                    connectionName = inputArray.pop();
	                    
	                    // Check if connection actually exists
	                    if(connectionMap.get(connectionName) != null) {
	                        id = connectionMap.get(connectionName).getID();
	                        return new DisconnectPacket(id);
	                    }
	                }
	                out.println("Usage: " + DISCONNECT_COMMAND);
	            }
	    
	            
	            else if(command.equals("/list")) {
	                // This command takes 0 arguments
	                if(inputArray.size() == 0) {
	                    out.println("#### Connected To ####");
	                    for(Entry<String, ConnectionInfo> con: connectionMap.entrySet()) {
	                        if(con.getValue().accepted)
	                            out.println(con.getKey());
	                    }
	                }
	                else
	                    out.println("Usage: " + LIST_COMMAND);
	            }
	       
	            
	            else if(command.equals("/msg")) {
	                // This command accepts 1 argument
	                if(inputArray.size() == 1) {
	                    connectionName = inputArray.pop();
	                    
	                    // Check if connection actually exists
	                    if(connectionMap.get(connectionName) != null) {
	                        sendMessageID = connectionMap.get(connectionName).getID();
	                    }
	                }
	                else
	                    out.println("Usage: " + MSG_COMMAND);
	            }
	            
	            
	            else if(command.equals("/pending")) {
	                // This command takes 0 arguments
	                if(inputArray.size() == 0) {
	                    out.println("#### Pending Connections ####");
	                    for(Entry<String, ConnectionInfo> con: connectionMap.entrySet()) {
	                        if(!con.getValue().accepted)
	                            out.println(con.getKey());
	                    }
	                }
	                else
	                    out.println("Usage: " + PENDING_COMMAND);
	            }
	                
	            
	            else if(command.equals("/quit")) {
	                System.exit(0);
	            }
	                
	            else {
	                out.println(VALID_COMMANDS);
	            }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Packet();
    }

    @Override
    public void send(Packet p) {
        String ip;
        int id, port;
        String connectionName = "";
    	if(p instanceof DisplayMessagePacket) {
    	    id = ((DisplayMessagePacket)p).getConnectionID();
    	    for(Entry<String, ConnectionInfo> con: connectionMap.entrySet()) {
                if(con.getValue().getID() == id)
                    connectionName = con.getKey();
            }
    		out.println("#### Incomming from " + connectionName + " ####");
    		out.println(((DisplayMessagePacket)p).getMessage());
    	}    
        
    	else if(p instanceof DisplayConnectionAcceptedPacket) {
    	    id = ((DisplayConnectionAcceptedPacket)p).getConnectionID();
    	    ip = ((DisplayConnectionAcceptedPacket)p).getIP();
    	    port = ((DisplayConnectionAcceptedPacket)p).getPort();
            for(Entry<String, ConnectionInfo> con: connectionMap.entrySet()) {
                ConnectionInfo ci = con.getValue();
                if(ci.getIP().equals(ip) && ci.getPort() == port) {
                    ci.setID(id);
                    ci.setAccepted(true);
                    connectionName = con.getKey();
                }
            }
        	out.println("#### You are now connected to " + connectionName + " ####");
        }
        
    	else if(p instanceof DisplayConnectionDeclinedPacket) {
    	    id = ((DisplayConnectionDeclinedPacket)p).getConnectionID();
    	    for(Entry<String, ConnectionInfo> con: connectionMap.entrySet()) {
                if(con.getValue().getID() == id)
                    connectionName = con.getKey();
            }
        	out.println("#### Declined connection from " + connectionName + " ####");
        }
    	
    	else if(p instanceof DisplayServerStartPacket) {
    	    port = ((DisplayServerStartPacket)p).getPort();
    		out.println("#### Server started on port: " + port + " ####");
    	}
    	
    	else if(p instanceof DisplayConnectionRequestPacket) {
    	    id = ((DisplayConnectionRequestPacket)p).getConnectionID();
    	    ip = ((DisplayConnectionRequestPacket)p).getIP();
    	    port = ((DisplayConnectionRequestPacket)p).getPort();
    	    
    	    ConnectionInfo ci = new ConnectionInfo(ip, port, false);
    	    connectionMap.put(ip + ":" + port, ci);
    	    ci.setID(id);
    	    
    		out.println("#### Incoming Request From " + ip + ":" + port + " ####");
    	}
    	
    	else if(p instanceof DisplayConnectionRequestedPacket) {
    		id = ((DisplayConnectionRequestedPacket)p).getConnectionID();
    		for(Entry<String, ConnectionInfo> con: connectionMap.entrySet()) {
                if(con.getValue().getID() == id)
                    connectionName = con.getKey();
            }
    		out.println("#### Connection Request Successfully Sent To " + connectionName + " ####" );
    	}
        
    	else if(p instanceof ConnectionRefusedErrorPacket) {
        	id = ((ConnectionRefusedErrorPacket)p).getConnectionID();
        	
        	for(Entry<String, ConnectionInfo> con: connectionMap.entrySet()) {
                if(con.getValue().getID() == id)
                    connectionName = con.getKey();
            }
        	
    	    out.println("#### "+ connectionName + " declined your connection request ####");
        	connectionMap.remove(connectionName);
    	}
        
    	else if(p instanceof UnknownHostErrorPacket) {
        	out.println("Usage: " + CONNECT_COMMAND);
        }
        
    	else if(p instanceof DisconnectErrorPacket) {
        	out.println("Usage: " + DISCONNECT_COMMAND);
        }
    }
}
