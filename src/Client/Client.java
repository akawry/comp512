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
	    String server = "localhost";
	    if (args.length == 1) 
			server = args[0]; 
	    else if (args.length != 0 &&  args.length != 1) 
		{
			System.out.println ("Usage: java client [rmihost]"); 
			System.exit(1); 
	    }
		
		try 
		{
			// get a reference to the rmiregistry
			Registry registry = LocateRegistry.getRegistry(server);
			// get the proxy and the remote reference by rmiregistry lookup
			rm = (IResourceManager) registry.lookup("akawry_MyGroupResourceManager");
			if(rm!=null)
			{
				System.out.println("Successful");
				System.out.println("Connected to RM");
				System.out.println("ResourceManager: "+rm.toString());
			}
			else
			{
				System.out.println("Unsuccessful");
			}
			// make call on remote method
		} 
		catch (Exception e) 
		{	
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
		
		// begin input loop 
		acceptInput();
	    
	}
}
