package Client; 

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
		boolean automatic = false ;
		int loopnb = 0 ;
		int trsec = 0 ;
		int x = 0 ;

		if (args.length == 2) { 
			serverhost = args[0].split(":")[0]; 
			serverport = Integer.parseInt( args[0].split(":")[1] ); 
			automatic = Boolean.valueOf(args[1].split(":")[0]) ;
			loopnb = Integer.parseInt( args[1].split(":")[1]) ;
			trsec = Integer.parseInt( args[1].split(":")[2]) ;
			x = Integer.parseInt( args[1].split(":")[3]) ;

		} else if (args.length > 2) {
			System.out.println ("Usage: java RMIClient [rmihost] [true/false:loopnb:tr_per_second(0 = no sleep):type]"); 
			System.exit(1); 
		}

		try 
		{
			Registry registry = LocateRegistry.getRegistry(serverhost,serverport);
			rm = (ResourceFrontend) registry.lookup("RMIMiddleware");
			System.out.println("[OK] Client successfully connected to server at " + serverhost  + " on port " + serverport);
		} 
		catch (Exception e) 
		{	
			System.err.println("[ERROR] Client exception while connecting to " + serverhost + " on port " + serverport + e.toString() ) ;
			e.printStackTrace();
			System.exit(1);
		}

		if (automatic) {
		    automaticInput(loopnb,trsec,x) ;
		} else {
		    manualInput();
		}

	}
}
