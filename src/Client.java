import java.net.ConnectException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

public class Client extends Thread {
    private ChatInterface ui;
    private ArrayList<Connection> connections;
    private ArrayList<ConnectionRequest> pendingConnections;
    private Hashtable<String,Connection> map;
    private BufferedReader in;
    private PrintWriter out;
    private Server server;

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
    
    private class Server extends Thread {
        private ServerSocket serverSocket;
        private int serverPort = 0;

        public Server() {
            try {
                serverSocket = new ServerSocket(serverPort);
                out.println("Server started on: " + serverSocket.getLocalPort());
            } catch (IOException e) { e.printStackTrace(); }     
        }
        
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
    
    private void acceptConnection(String connectionName) {
        synchronized(pendingConnections) {
            for(ConnectionRequest cr: pendingConnections) {
                if(cr.equals(connectionName)) {
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
    
    private void connect(String ip, int port, String name) {
        try {
            connect(new Socket(ip, port), name);
        } catch (ConnectException e) {
            out.println("#### Connection Refused ####");
        } catch(IOException e) {
            out.println("Usage: /connect <ip> <port> <name>");
        }
    }

    private void connect(Socket s, String name) {
        Connection newConnection = new Connection(s, name, out);
        map.put(name, newConnection);
        connections.add(newConnection);
    }

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

    private void disconnect(String connectionName) {
        try {
            connections.remove(map.get(connectionName));
        } catch(Exception e) {
            out.println("Usage: /disconnect <ip/nickname>");
        }
    }
    
    private void printAcceptedConnections() {
        out.println("#### Connected To ####");
        for(Connection con: connections) {
            if(con.isClosed())
                connections.remove(con);
            else
                out.println(con.toString());
        }
    }
    
    private void printPendingConnections() {
        out.println("#### Pending Connections ####");
        synchronized(pendingConnections) {
            for(ConnectionRequest cr: pendingConnections) {
                out.println(cr.toString());
            }
        }
    }
    
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
    
    public static void main(String[] args) {
        Client c1 = new Client(new CommandInterface());
    }
}
