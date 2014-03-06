import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Connection extends Thread {
    private Socket s;
    private BufferedReader in;
    private PrintWriter sysout, out;
    private String name;
    
    public Connection(Socket s, String name, PrintWriter out) {
        this.s = s;
        this.name = name;
        this.sysout = out;
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
    
    public void sendMessage(String message) {
        out.println(message);
    }
    
    public String toString() {
        return name;
    }
    
    public boolean isClosed() {
        return s.isClosed();
    }
}
