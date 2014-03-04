import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class Connection extends Thread {
    private Socket s;
    private BufferedReader in;
    private PrintWriter out, send_message;
    private String name;
    
    public Connection(Socket s, String name, BufferedReader in, PrintWriter out) {
        this.s = s;
        this.name = name;
        this.out = out;
        this.in = in;
        try {
            this.send_message = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e) { e.printStackTrace(); }
        start();
    }
    
    public void run() {
        try {
            String line;
            while((line = in.readLine()) != null) {
                out.println(line);
            }
            s.close();
        } catch(IOException e) { e.printStackTrace(); }
    }
    
    public void sendMessage(String message) {
        send_message.println(message);
    }
    
    public String toString() {
        return name;
    }
    
    public boolean isClosed() {
        return s.isClosed();
    }
}
