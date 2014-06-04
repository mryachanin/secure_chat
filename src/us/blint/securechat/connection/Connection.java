package us.blint.securechat.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.SecureRandom;

import us.blint.securechat.encryption.AES.AES;
import us.blint.securechat.encryption.RSA.RSAPublicKey;
import us.blint.securechat.encryption.RSA.RSAPrivateKey;
import us.blint.securechat.ui.ChatInterface;
import us.blint.securechat.ui.packet.display.DisplayConnectionAcceptedPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionDeclinedPacket;
import us.blint.securechat.ui.packet.display.DisplayConnectionRequestPacket;
import us.blint.securechat.ui.packet.display.DisplayMessagePacket;

/**
 *  <pre>
 *  Defines a connection
 *
 *  When this thread starts, it will prompt the user to accept the connection
 *  if the user did not initiate it.
 *      Case: User initiates request
 *          The thread will continuously probe for input from the socket 
 *              associated with this connection and print the input to the user. 
 *          When the BufferedReader returns null, the socket is closed.
 *      
 *      Case: Other user initiates request:
 *          Request the user to accept a connection. The thread will be blocked
 *              until the user either confirms or denies the request.
 *          If the connection is accepted, the thread will continuously probe for 
 *              input from the socket associated with this connection and print
 *              the input to the user. When the BufferedReader returns null, 
 *              the socket is closed.
 *          If the user does not accept the connection, the socket is closed.
 *  </pre>
 */
public class Connection extends Thread {
    private Socket s;
    private int connectionID;
    private ConnectionManager cm;
    private ChatInterface ui;
    private boolean accepted;
    private String ip;
    private int port;
    private boolean finished;
    private BufferedReader in;
    private PrintWriter out;
    private AES aes;
    private SecureRandom rand;
    
    /**
     *  Initialize variables
     *  Starts main thread
     * 
     *  @param s                   Socket between the client and another user
     *  @param connectionID        ID of this connection
     *  @param connectionManager   Manages all connections
     *  @param accepted            True if the user initiated the connection
     */
    public Connection(Socket s, int connectionID, ConnectionManager cm, ChatInterface ui, boolean accepted) {
        this.s = s;
        this.connectionID = connectionID;
        this.cm = cm;
        this.ui = ui;
        this.accepted = accepted;
        this.ip = s.getInetAddress().getHostName();
        this.port = s.getPort();
        this.finished = false;
        this.rand = new SecureRandom();
        
        try {
            this.in = new BufferedReader(
                          new InputStreamReader(s.getInputStream()));
            this.out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) { e.printStackTrace(); }
        start();
    }
    
    public void run() {
        if(!accepted) {
            ui.send(new DisplayConnectionRequestPacket(connectionID, ip, port));
            boolean allowed = cm.requestConnection(connectionID);
            if(!allowed) {
                ui.send(new DisplayConnectionDeclinedPacket(connectionID));
                try {
                    s.close();
                } catch (IOException e) { e.printStackTrace(); }
            }   
            else {
            	try {
	                RSAPrivateKey RSA_key = new RSAPrivateKey();
	                sendMessage(RSA_key.getPublicExponent());
	                sendMessage(RSA_key.getMod());
	                sendMessage(RSA_key.encrypt(RSA_key.getPublicExponent()));
	                sendMessage(RSA_key.encrypt(RSA_key.getMod()));
	                String encrypted_AES_key = in.readLine();
	                byte[] AES_key = RSA_key.decrypt(encrypted_AES_key).getBytes();
	                this.aes = new AES(AES_key);
	                ui.send(new DisplayConnectionAcceptedPacket(connectionID));
            	} catch (IOException e) {}
            }
        }
        else {
        	try {
				String RSA_public_exponent = in.readLine();
				String mod = in.readLine();
				String RSA_public_exponent_signature = in.readLine();
				String mod_signature = in.readLine();
        		RSAPublicKey RSA_key = new RSAPublicKey(RSA_public_exponent, mod);
        		if (!RSA_public_exponent.equals(RSA_key.decrypt(RSA_public_exponent_signature)) || !mod.equals(RSA_key.decrypt(mod_signature))) {
        			ui.send(new DisplayConnectionDeclinedPacket(connectionID));
        			close();
        		}
        		else {
		        	byte[] AES_key = new byte[16];
					rand.nextBytes(AES_key);
		        	String encrypted_AES_key = RSA_key.encrypt(AES_key.toString());
		        	sendMessage(encrypted_AES_key);
		        	this.aes = new AES(AES_key);
					ui.send(new DisplayConnectionAcceptedPacket(connectionID));
        		}
			} catch (IOException e) {}
        }
        try {
            String line;
            while((line = in.readLine()) != null && !finished) {
            	line = new String(aes.decrypt(line.getBytes()));
                ui.send(new DisplayMessagePacket(line, connectionID));
            }
            s.close();
        } catch(IOException e) { e.printStackTrace(); }
    }
    
    /**
     *  Sends a message to the socket's output stream
     * 
     *  @param message   String to send over the socket
     */
    public void sendMessage(String message) {
        out.println(aes.encrypt(message.getBytes()));
    }
    
    /**
     *  Returns the ID of this connection
     * 
     *  @return connectionID
     */
    public int getConnectionID() {
        return connectionID;
    }
    
    /**
     *  Sets finished to true. This will exit the while loop in run() and 
     *  close the socket.
     *  
     *  @throws IOException
     */
    public void close() throws IOException {
        finished = true;
    }
}
