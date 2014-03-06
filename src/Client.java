import java.net.ConnectException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

/**
 *  Defines a client that can accept connections, request connections,
 *      and send data securely over connections to other clients. 
 *      
 *  When this thread starts, it will:
 *      Continuously listen for commands and execute them
 */
public class Client extends Thread {
    private ChatInterface ui;
    private ArrayList<Connection> connections;
    private ArrayList<ConnectionRequest> pendingConnections;
    private Hashtable<String,Connection> map;
    private BufferedReader in;
    private PrintWriter out;
    private Server server;

    /**
     *  Initializes variables
     *  Starts server thread
     *  Starts main thread
     *  
     *  @param ui Defines the user interface
     */
    public Client(ChatInterface ui) {
        this.ui = ui;
        connections = new ArrayList<Connection>();
        map = new Hashtable<String,Connection>();
        in = ui.getInput();
        out = ui.getOutput();
        server = new Server();
        server.start();
        start();
    }
    
    /**
     *  Defines a server that will constantly listen for connection requests
     *
     *  When started as a thread, this will:
     *      Continuously listen for new connection requests.
     *      When a new connection is found, the server will request the user to 
     *          accept the connection. If the user accepts, a new connection is
     *          made; else, the socket is closed
     */
    private class Server extends Thread {
        private ServerSocket serverSocket;
        private int serverPort = 0;

        /**
         *  Instantiates a new server socket
         *  Prints the port it was started on
         */
        public Server() {
            try {
                serverSocket = new ServerSocket(serverPort);
                out.println("Server started on: " + serverSocket.getLocalPort());
            } catch (IOException e) { e.printStackTrace(); }     
        }
        
        @Override
        public void run() {
            Socket clientSocket;
            try {
                clientSocket = serverSocket.accept();
                String ip = clientSocket.getInetAddress().toString();
                out.println("#### Incoming Request From " + ip + " ####");

                boolean allowed = ui.requestConnection(ip);
                if(!allowed) {
                    out.println("#### Refused connection from " + ip + " ####");
                    try {
                        clientSocket.close();
                    } catch (IOException e) { e.printStackTrace(); }
                } else {
                    connect(clientSocket, ip);
                    out.println("#### You are now connected to " + ip + " ####");
                }
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
    
    @Override
    public void run() {
        String input;
        try {
            while((input = in.readLine()) != null) {
                String[] inputArray = input.toLowerCase().split("\\s+");
                pendingConnections = ui.getPendingConnections();
                switch(inputArray[0]) {
                    case "/accept":
                        acceptConnection(inputArray[1]);
                        break;

                    case "/connect":
                        try {
                            connect(inputArray[1], 
                                        Integer.parseInt(inputArray[2]), 
                                        inputArray[3]);
                            
                        } catch(NumberFormatException e) {
                            out.println("Usage: connect <ip> <port> <name>");
                        }
                        break;

                    case "/decline":
                        declineConnection(inputArray[1]);
                        break;

                    case "/disconnect":
                        disconnect(inputArray[1]);
                        break;

                    case "/list":
                        printAcceptedConnections();
                        break;
   
                    case "/msg":
                        sendMessage(inputArray[1]);
                        break;
                    
                    case "/pending":
                        printPendingConnections();
                        break;
                        
                    case "/quit":
                        System.exit(0);
                        
                    default:
                        out.println("Valid commands:");
                        out.print("/accept <ip>");
                        out.print("/connect <ip> <port> <nickname>");
                        out.print("/decline <ip>");
                        out.print("/disconnect <ip/nickname>");
                        out.println("/list");
                        out.println("/msg <ip/nickname>");
                        out.println("/pending");
                        out.println("/quit");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     *  Called with /accept
     *  
     *  Allows a connection to be instantiated
     * 
     *  @param connectionRequestName Name of the request to accept
     */
    private void acceptConnection(String connectionRequestName) {
        synchronized(pendingConnections) {
            for(ConnectionRequest cr: pendingConnections) {
                if(cr.equals(connectionRequestName)) {
                    cr.setAccepted(true);
                    synchronized(cr) {
                        cr.notify();
                    }
                    pendingConnections.remove(cr);
                    break;
                }
            }
        }
    }
    
    /**
     *  Called with /connect
     *  
     *  passes overloaded method a new socket and the connection name to
     *      instantiate a new connection
     *  
     *  @param ip IP Address of the socket for a new connection
     *  @param port Port of the socket for a new connection
     *  @param connectionName Name of the connection for ease of reference
     */
    private void connect(String ip, int port, String connectionName) {
        try {
            connect(new Socket(ip, port), connectionName);
        } catch (ConnectException e) {
            out.println("#### Connection Refused ####");
        } catch(IOException e) {
            out.println("Usage: /connect <ip> <port> <name>");
        }
    }

    /**
     *  Instantiates a new connection and associates it with a connection name
     * 
     *  @param s Socket for a new connection
     *  @param connectionName Name of the connection for ease of reference
     */
    private void connect(Socket s, String connectionName) {
        Connection newConnection = new Connection(s, connectionName, out);
        map.put(connectionName, newConnection);
        connections.add(newConnection);
    }

    /**
     *  Called with /decline
     *  
     *  Does not allow a connection to be instantiated
     * 
     *  @param connectionRequestName Name of the request to decline
     */
    private void declineConnection(String connectionName) {
        synchronized(pendingConnections) {
            for(ConnectionRequest cr: pendingConnections) {
                if(cr.equals(connectionName)) {
                    synchronized(cr) {
                        cr.notify();
                    }
                    pendingConnections.remove(cr);
                    break;
                }
            }
        }
    }

    /**
     *  Called with /disconnect
     *  
     *  Disconnect a current connection
     *  
     *  @param connectionName Name of the connection to disconnect from
     */
    private void disconnect(String connectionName) {
        try {
            connections.remove(map.get(connectionName));
        } catch(Exception e) {
            out.println("Usage: /disconnect <ip/nickname>");
        }
    }
    
    /**
     *  Prints the names of all currently accepted connections
     */
    private void printAcceptedConnections() {
        out.println("#### Connected To ####");
        for(Connection con: connections) {
            if(con.isClosed())
                connections.remove(con);
            else
                out.println(con.getConnectionName());
        }
    }
    
    /**
     *  Prints the names of all pending connections
     */
    private void printPendingConnections() {
        out.println("#### Pending Connections ####");
        synchronized(pendingConnections) {
            for(ConnectionRequest cr: pendingConnections) {
                out.println(cr.getConnectionName());
            }
        }
    }
    
    /**
     *  Continuously sends messages to a connection until user types '/close'
     * 
     *  @param connectionName Name of the connection to send messages
     */
    private void sendMessage(String connectionName) {
        try {
            Connection c = map.get(connectionName);
            while(true) {
                String msg = in.readLine();
                if(msg.equals("/close"))
                    break;
                c.sendMessage(msg);
            }
        } catch(Exception e) {
            out.println("Usage: /msg <name>");
            e.printStackTrace();
        }
    }
    
    /**
     *  Instantiates a new client using the command interface
     *  
     *  @param args These will all be ignored
     */
    public static void main(String[] args) {
        new Client(new CommandInterface());
    }
}
