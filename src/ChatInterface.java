import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *  This defines how an interface for the chat program should function.
 */
public interface ChatInterface {

	/**
	 *  Request the user to accept a connection. This method should block the
	 *  calling thread until the user either confirms or denies the request.
	 *
	 *  @param connectionName The identifier for the connection
	 *
	 *  @return true is the user accepts the connection, false otherwise
	 */
	public boolean requestConnection(String connectionName);

	/**
	 *  The stream to read user input from.
	 *
	 *  @return The BufferedReader the user input comes on
	 */
	public BufferedReader getInput();

	/**
	 *  The stream to send output to the user.
	 *
	 *  @return The PrintWriter to send the user output
	 */
	public PrintWriter getOutput();
	
	public ArrayList<ConnectionRequest> getPendingConnections();

}