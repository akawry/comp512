package ResImpl.TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import ResImpl.AbstractResourceManager;

public abstract class AbstractTCPResourceManager {

	/**
	 * Handle a message sent from a client.
	 * This is a command string sent from the client 
	 * @param line the command 
	 * @return the response string to send to the client 
	 */
	public abstract String processInput(String line);

	/**
	 * Start receiving messages on the specified port
	 * Messages are handled by processInput
	 * One new thread per connection 
	 * @param port the port to listen on
	 */
	protected void listen(int port){
        ServerSocket serverSocket = null;
        boolean listening = true;
 
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: "+port);
            System.exit(-1);
        }
 
        while (listening) {
			try {
				new MultiServerThread(serverSocket.accept(), this).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
 
        try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send a message to the host and port 
	 * @param msg the message to send 
	 * @param host the host to send to 
	 * @param port the port on the host to send to 
	 */
	protected String send(String msg, String host, int port){
		
		String result = null;
		
		try {
			Socket socket = new Socket(host, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(msg);
            result = in.readLine();
            out.close();
            in.close();
            socket.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * Convenience method for reconstructing
	 * client messages from method calls 
	 * @param args
	 * @return
	 */
	protected String concat(Object ... args){
		String result = "";
		for (int i = 0; i < args.length; i++){
			result += args[i].toString();
			if (i < args.length - 1)
				result += ",";
		}
		return result; 
	}
	
}
