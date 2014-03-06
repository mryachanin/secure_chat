import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *  Defines a connection
 *
 *  When this thread starts, it will continuously probe for input from the 
 *      socket associated with this connection and print the input to the user.
 *      When the BufferedReader returns null, the socket is closed
 */
public class Connection extends Thread {
    private Socket s;
    private BufferedReader in;
    private PrintWriter sysout, out;
    private String name;
    
    /**
     *  Initializes variables
     *  Starts main thread
     * 
     *  @param s      Socket between the client and another user
     *  @param name   Name of this connection
     *  @param sysout Output stream to user
     */
    public Connection(Socket s, String name, PrintWriter sysout) {
        this.s = s;
        this.name = name;
        this.sysout = sysout;
        try {
            this.in = new BufferedReader(
                          new InputStreamReader(s.getInputStream()));
            this.out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) { e.printStackTrace(); }
        start();
    }
    
    public void run() {
        try {
            String line;
            while((line = in.readLine()) != null) {
                sysout.println(line);
            }
            s.close();
        } catch(IOException e) { e.printStackTrace(); }
    }
    
    /**
     *  Sends a message to the socket's output stream
     * 
     *  @param message String to send over the socket
     */
    public void sendMessage(String message) {
        out.println(message);
    }
    
    /**
     *  Returns the name of this connection
     * 
     *  @return Name of this connection
     */
    public String getConnectionName() {
        return name;
    }
    
    /**
     *  Returns if the socket is closed
     * 
     *  @return Boolean representing if the socket is closed
     */
    public boolean isClosed() {
        return s.isClosed();
    }
}
