import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *  Interface meant to be used with the command line
 */
public class CommandInterface implements ChatInterface {
    BufferedReader in;
    PrintWriter out;
    ArrayList<ConnectionRequest> pendingConnections;
    
    /**
     *  Initializes the default input and output stream (system.in / system.out)
     */
    public CommandInterface() {
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out, true);
        pendingConnections = new ArrayList<ConnectionRequest>();
    }
    
    @Override
    public boolean requestConnection(String connectionName) {
        ConnectionRequest request = new ConnectionRequest(connectionName);
        synchronized(pendingConnections) {
            pendingConnections.add(request);
        }
        try {
            synchronized(request) {
                request.wait();
            }
        } catch (InterruptedException e) { e.printStackTrace(); }
        
        return request.isAccepted();
    }

    @Override
    public BufferedReader getInput() {
        return in;
    }

    @Override
    public PrintWriter getOutput() {
        return out;
    }
    
    @Override
    public ArrayList<ConnectionRequest> getPendingConnections() {
        return pendingConnections;
    }
}
