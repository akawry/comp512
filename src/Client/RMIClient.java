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
		String[] serverhosts;
		int[] serverports; 
		boolean automatic = false ;
    int clientnb = 0 ;
		int loopnb = 0 ;
		int trsec = 0 ;
		int type = 0 ;

		if (args.length == 2) {
			String[] set = args[0].split(",");
			serverhosts = new String[set.length];
			serverports = new int[set.length];
			for (int i = 0; i < set.length; i++){
				serverhosts[i] = set[i].split(",")[0];
				serverports[i] = Integer.parseInt(set[i].split(":")[1]);
			}
			
			automatic = Boolean.valueOf(args[1].split(":")[0]) ;
			clientnb = Integer.parseInt( args[1].split(":")[1]) ;
			loopnb = Integer.parseInt( args[1].split(":")[2]) ;
			trsec = Integer.parseInt( args[1].split(":")[3]) ;
			type = Integer.parseInt( args[1].split(":")[4]) ;

			try 
			{
				for (int i = 0; i < serverhosts.length; i++){
					Registry registry = LocateRegistry.getRegistry(serverhosts[i],serverports[i]);
					rms.add((ResourceFrontend) registry.lookup("RMIMiddleware"));
				}
				rm = rms.get(0);
				//System.out.println("[OK] Client successfully connected to server at " + serverhost  + " on port " + serverport);
			} 
			catch (Exception e) 
			{	
				System.err.println("[ERROR] Client exception while getting rmi object" ) ;
				e.printStackTrace();
				System.exit(1);
			}
	
			if (automatic) {
			    automaticInput(clientnb,loopnb,trsec,type) ;
			} else {
			    manualInput();
			}
		} else if (args.length > 2) {
			System.out.println ("Usage: java RMIClient [rmihost] [true/false:loopnb:tr_per_second(0 = no sleep):type]"); 
			System.exit(1); 
		}
	}
}
