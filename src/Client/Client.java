package Client;

// Client.java


import java.rmi.*;
import ResInterface.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.*;
import java.io.*;


public class Client extends AbstractClient
{

	public static void main(String args[])
	{
		String serverhost = "localhost";
		int serverport = 1099; 

		if (args.length == 1) { 
			serverhost = args[0].split(":")[0]; 
			serverport = Integer.parseInt( args[0].split(":")[1] ); 
		} else if (args.length > 1) {
			System.out.println ("Usage: java client [rmihost]"); 
			System.exit(1); 
		}

		try 
		{
			Registry registry = LocateRegistry.getRegistry(serverhost,serverport);
			rm = (IResourceManager) registry.lookup("akawry_MyGroupResourceManager");
			System.out.println("Client successfully connected to server at " + serverhost  + " on port " + serverport);
		} 
		catch (Exception e) 
		{	
			System.err.println("[ERROR] Client exception: " + e.toString());
			e.printStackTrace();
		}

		// begin input loop 
		acceptInput();

	}
}
