import java.io.*;
import java.net.Socket;
import java.util.NavigableSet;
import java.util.TreeMap;

/**
 * Handles communication between the server and one client, for SketchServer
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			while (true) {
				// Tell the client the current state of the world
				// TODO: YOUR CODE HERE
				//Broadcasts all shapes to client
				TreeMap<Integer, Shape> map = server.getSketch().getShapes();    //sends client shapes low order first
				NavigableSet<Integer> navigableSet = map.navigableKeySet();
				System.out.println("Broadcasting");
				for (Integer i : navigableSet) {
					System.out.println("Broadcast: " + i + " " + map.get(i).toString());
					server.broadcast(i + " " + map.get(i).toString());
				}


				// Keep getting and handling messages from the client
				// TODO: YOUR CODE HERE
				//Adds shapes received from clients to the shape map
				String input = in.readLine();
				System.out.println("SSC Input");
				if(input!=null) {
					if (input.split(" ")[1].equals("delete")) {
						server.broadcast(input);
					}
					server.receiveMessage(input);   //adds shape to server master sketch
				} else {
					// Clean up -- note that also remove self from server's list so it doesn't broadcast here
					server.removeCommunicator(this);
					out.close();
					in.close();
					sock.close();
					break;
				}
			}


		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
