package Client; 
// Client.java


import java.rmi.*;
import ResInterface.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.*;
import java.io.*;


public class RMIClient extends AbstractClient
{

	public static void main(String args[])
	{
		String serverhost = "localhost";
		int serverport = 1099; 

		if (args.length == 1) { 
			serverhost = args[0].split(":")[0]; 
			serverport = Integer.parseInt( args[0].split(":")[1] ); 
		} else if (args.length > 1) {
			System.out.println ("Usage: java RMIClient [rmihost]"); 
			System.exit(1); 
		}

		try 
		{
			Registry registry = LocateRegistry.getRegistry(serverhost,serverport);
			rm = (IResourceManager) registry.lookup("RMIMiddleware");
			System.out.println("[OK] Client successfully connected to server at " + serverhost  + " on port " + serverport);
		} 
		catch (Exception e) 
		{	
			System.err.println("[ERROR] Client exception while connecting to " + serverhost + " on port " + serverport + e.toString() ) ;
			e.printStackTrace();
			System.exit(1);
		}

		// begin input loop 
		acceptInput();

	}
}
